/*
 * Copyright 2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package powermock.examples.tutorial.partialmocking.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ServiceProducer extends ProviderArtifact
{
   private final Set<DataProducer> dataProducingArtifacts;

   public ServiceProducer(int id, String name, DataProducer... dataProducingArtifacts)
   {
      super(id, name);
      this.dataProducingArtifacts = new HashSet<DataProducer>();

      for (DataProducer dataProducingArtifact : dataProducingArtifacts) {
         this.dataProducingArtifacts.add(dataProducingArtifact);
      }
   }

   public Set<DataProducer> getDataProducers()
   {
      return Collections.unmodifiableSet(dataProducingArtifacts);
   }
}
