/*
 * Copyright 2018 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thoughtworks.go.apiv6.admin.pipelineconfig.representers.materials;

import com.thoughtworks.go.api.base.OutputWriter;
import com.thoughtworks.go.api.representers.JsonReader;
import com.thoughtworks.go.config.CruiseConfig;
import com.thoughtworks.go.config.materials.Filter;
import com.thoughtworks.go.config.materials.PluggableSCMMaterialConfig;

import java.util.Map;

public class PluggableScmMaterialRepresenter {

    public static void toJSON(OutputWriter jsonWriter, PluggableSCMMaterialConfig pluggableSCMMaterialConfig) {
        jsonWriter.add("ref", pluggableSCMMaterialConfig.getScmId());
        jsonWriter.addChild("filter", filterWriter -> FilterRepresenter.toJSON(filterWriter, pluggableSCMMaterialConfig.filter()));
        jsonWriter.add("destination", pluggableSCMMaterialConfig.getFolder());
    }

    public static PluggableSCMMaterialConfig fromJSON(JsonReader jsonReader, Map<String, Object> options) {
        if (jsonReader == null) {
            return null;
        }
        PluggableSCMMaterialConfig pluggableSCMMaterialConfig = new PluggableSCMMaterialConfig();
        // Pass along options or the cruise config object.
        CruiseConfig goConfig = (CruiseConfig) options.get("goConfig");
        if (goConfig != null) {
            String ref = jsonReader.getString("ref");
            pluggableSCMMaterialConfig.setSCMConfig(goConfig.getSCMs().find(ref));
            pluggableSCMMaterialConfig.setScmId(ref);

        }

        jsonReader.readStringIfPresent("destination", pluggableSCMMaterialConfig::setFolder);
        if (jsonReader.hasJsonObject("filter")) {
            Filter filter = FilterRepresenter.fromJSON(jsonReader.readJsonObject("filter"));
            pluggableSCMMaterialConfig.setFilter(filter);
        }
        return pluggableSCMMaterialConfig;
    }
}
