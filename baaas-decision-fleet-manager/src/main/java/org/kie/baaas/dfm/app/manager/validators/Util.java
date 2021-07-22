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

package org.kie.baaas.dfm.app.manager.validators;

import io.fabric8.kubernetes.client.utils.KubernetesResourceUtil;

public class Util {

    public static String sanitizeName(String name) {
        if (name == null || name.isEmpty()) {
            return name;
        }
        String tmp = KubernetesResourceUtil.sanitizeName(name.toLowerCase());
        return tmp.charAt(0) >= '0' && tmp.charAt(0) <= '9' ? "num-" + tmp : tmp;
    }
}
