/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.extension.example.reactor;

import java.util.HashMap;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;

public class KpiService {

  private static KpiService INSTANCE = new KpiService();

  private final Map<String, Long> kpis = new HashMap<>();

  private KpiService() {
  }

  public static KpiService getInstance() {
    return INSTANCE;
  }

  public void increment(String name, DelegateExecution execution) {
    if (kpis.containsKey(name)) {
      Long value = kpis.get(name);
      kpis.put(name, value + 1);

    } else {
      kpis.put(name, 1L);
    }
  }

  public Long getKpi(String name) {
    if (kpis.containsKey(name)) {
      return kpis.get(name);

    } else {
      throw new IllegalArgumentException("no kpi with name: " + name);
    }
  }

}
