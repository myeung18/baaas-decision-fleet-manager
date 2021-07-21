/*
 * Copyright 2021 Red Hat, Inc. and/or its affiliates.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package org.kie.baaas.dfm.app.manager.kafkaservice;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kie.baaas.dfm.app.exceptions.DecisionFleetManagerException;
import org.kie.baaas.dfm.app.managedservices.ManagedServicesClient;
import org.kie.baaas.dfm.app.manager.KafkaService;
import org.kie.baaas.dfm.app.vault.VaultService;

import io.quarkus.runtime.Startup;

@ApplicationScoped
public class KafkaServiceProducer {

    private volatile KafkaServiceType type = KafkaServiceType.DISABLED;

    @ConfigProperty(name = "baaas.dfm.kafka.service.type")
    String serviceType;

    @Inject
    ManagedServicesClient msc;

    @Inject
    VaultService vaultService;

    @Startup
    @ApplicationScoped
    public KafkaService produceKafkaService() {
        KafkaService kc;
        if (KafkaServiceType.OPERATE_FIRST.equalValue(serviceType)) {
            kc = new OperateFirstKafkaService();
            this.type = KafkaServiceType.OPERATE_FIRST;
        } else if (KafkaServiceType.MANAGED_KAFKA.equalValue(serviceType)) {
            kc = new ManagedKafkaService(msc, vaultService);
            this.type = KafkaServiceType.MANAGED_KAFKA;
        } else if (KafkaServiceType.DISABLED.equalValue(serviceType)) {
            this.type = KafkaServiceType.DISABLED;
            kc = new DisabledKafkaService();
        } else {
            throw new DecisionFleetManagerException(String.format("invalid kafka service type:%s, failed to produce Kafka Service.", serviceType));
        }
        return kc;
    }

    public boolean isKafkaServiceEnabled() {
        return this.type != KafkaServiceType.DISABLED;
    }
}
