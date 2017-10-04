/*
 * Copyright (c) 2011-2014 Pivotal Software, Inc.
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

package org.camunda.bpm.extension.reactor.projectreactor.io.codec;

import java.util.function.Consumer;
import java.util.function.Function;
import org.camunda.bpm.extension.reactor.projectreactor.io.buffer.Buffer;

/**
 * A simple {@link Codec} implementation that turns a {@link Buffer} into a {@code byte[]} and
 * visa-versa.
 *
 * @author Jon Brisbin
 * @author Stephane Maldini
 */
public class ByteArrayCodec extends BufferCodec<byte[], byte[]> {

	@Override
	public Function<Buffer, byte[]> decoder(final Consumer<byte[]> next) {
		return new Function<Buffer, byte[]>() {
			@Override
			public byte[] apply(Buffer buffer) {
				byte[] bytes = buffer.asBytes();
				buffer.skip(bytes.length);
				if (null != next) {
					next.accept(bytes);
					return null;
				} else {
					return bytes;
				}
			}
		};
	}

	@Override
	public Buffer apply(byte[] bytes) {
		return Buffer.wrap(bytes);
	}

}
