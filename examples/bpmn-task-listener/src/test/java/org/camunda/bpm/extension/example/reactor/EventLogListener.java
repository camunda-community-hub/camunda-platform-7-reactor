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

import static org.slf4j.LoggerFactory.getLogger;

import org.camunda.bpm.extension.reactor.event.DelegateEvent;
import org.camunda.bpm.extension.reactor.event.DelegateEventConsumer;
import org.slf4j.Logger;

public final class EventLogListener extends DelegateEventConsumer {

  private final Logger LOGGER = getLogger(this.getClass());

  @Override
  public void accept(DelegateEvent delegateEvent) {
    LOGGER.debug(delegateEvent.toString());
  }
}