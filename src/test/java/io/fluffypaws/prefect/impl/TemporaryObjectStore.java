/*
 *  Copyright 2016 FluffyPaws Inc. (http://www.fluffypaws.io)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.fluffypaws.prefect.impl;

import java.util.Properties;

import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;

import io.fluffypaws.prefect.api.Key;
import io.fluffypaws.prefect.api.KeyValueStore;
import io.fluffypaws.prefect.api.Object;
import io.fluffypaws.prefect.api.ObjectStore;
import io.fluffypaws.prefect.api.StoreException;
import io.fluffypaws.prefect.api.StoreFactory;
import static org.assertj.core.api.Assertions.assertThat;

public class TemporaryObjectStore extends ExternalResource implements ObjectStore {

    private TemporaryFolder folder;
    private ObjectStore objectStore;

    public TemporaryObjectStore(TemporaryFolder folder) {
        this.folder = folder;
    }

    @Override
    protected void before() throws Throwable {
        Properties properties = new Properties();
        properties.setProperty(StoreFactory.STORE_DIRECTORY_KEY, folder.getRoot().getAbsolutePath());
        properties.setProperty(ObjectStoreImpl.STORE_LIST_COMPACTING_THRESHOLD_KEY, String.valueOf(3));
        objectStore = StoreFactory.createObjectStore(properties);
        assertThat(objectStore).isNotNull();
    }

    @Override
    public Object getRoot() throws StoreException {
        return objectStore.getRoot();
    }

    @Override
    public Object get(final Key key) throws StoreException {
        return objectStore.get(key);
    }

    @Override
    public KeyValueStore getKeyValueStore() {
        return objectStore.getKeyValueStore();
    }

}