/*
 * Copyright (c) 2011-2015 Pivotal Software, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.camunda.bpm.extension.reactor.projectreactor.bus.registry;

import org.camunda.bpm.extension.reactor.projectreactor.bus.selector.Selector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * A naive caching Registry implementation for use in situations that the default {@code CachingRegistry} can't be used
 * due to its reliance on the gs-collections library. Not designed for high throughput but for use on things like
 * handheld devices, where the synchronized nature of the registry won't affect performance much.
 */
public class SimpleCachingRegistry<K, V> implements Registry<K, V> {

	private final ConcurrentHashMap<Object, List<Registration<K, ? extends V>>> cache         = new
			ConcurrentHashMap<>();
	private final ConcurrentHashMap<Selector<K>, List<Registration<K, ? extends V>>> registrations = new
			ConcurrentHashMap<>();

	private final boolean     useCache;
	private final boolean     cacheNotFound;
	private final Consumer<K> onNotFound;

	SimpleCachingRegistry(boolean useCache, boolean cacheNotFound, Consumer<K> onNotFound) {
		this.useCache = useCache;
		this.cacheNotFound = cacheNotFound;
		this.onNotFound = onNotFound;
	}

	@Override
	public synchronized Registration<K, V> register(final Selector<K> sel, V obj) {
		List<Registration<K, ? extends V>> regs;
		if (null == (regs = registrations.get(sel))) {
			regs = registrations.computeIfAbsent(sel, s -> new ArrayList<Registration<K, ? extends V>>());
		}

		Registration<K, V> reg = new CachableRegistration<>(sel, obj, new Runnable() {
			@Override
			public void run() {
				registrations.remove(sel);
				cache.clear();
			}
		});
		regs.add(reg);

		return reg;
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized boolean unregister(Object key) {
		boolean found = false;
		for (Selector sel : registrations.keySet()) {
			if (!sel.matches(key)) {
				continue;
			}
			if (null != registrations.remove(sel) && !found) {
				found = true;
			}
		}
		if (useCache)
			cache.remove(key);
		return found;
	}

	@Override
	@SuppressWarnings("unchecked")
	public synchronized List<Registration<K, ? extends V>> select(final K key) {
		List<Registration<K, ? extends V>> selectedRegs;
		if (null != (selectedRegs = cache.get(key))) {
			return selectedRegs;
		}

    final List<Registration<K, ? extends V>> regs = new ArrayList<Registration<K, ? extends V>>();
    registrations.forEach((s,l) -> regs.addAll(l));

		if (regs.isEmpty() && null != onNotFound) {
			onNotFound.accept(key);
		}
		if (useCache && (!regs.isEmpty() || cacheNotFound)) {
			cache.put(key, regs);
		}

		return regs;
	}

	@Override
	public synchronized void clear() {
		cache.clear();
		registrations.clear();
	}

	@Override
	public synchronized Iterator<Registration<K, ? extends V>> iterator() {
		final List<Registration<K, ? extends V>> regs = new ArrayList<Registration<K, ? extends V>>();
		registrations.forEach((s,l) -> regs.addAll(l));
		return regs.iterator();
	}

}
