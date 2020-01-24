/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.tooling.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.camel.util.json.JsonObject;
import org.apache.camel.util.json.Jsoner;

public class ComponentModel extends BaseModel<ComponentModel.ComponentOptionModel> {

    protected String scheme;
    protected String extendsScheme;
    protected String alternativeSchemes;
    protected String syntax;
    protected String alternativeSyntax;
    protected boolean async;
    protected boolean consumerOnly;
    protected boolean producerOnly;
    protected boolean lenientProperties;
    protected String verifiers;
    protected String groupId;
    protected String artifactId;
    protected String version;

    protected final List<EndpointOptionModel> endpointOptions = new ArrayList<>();

    public static ComponentModel generateComponentModel(String json) {
        JsonObject obj = deserialize(json);
        JsonObject mobj = (JsonObject) obj.get("component");
        ComponentModel model = new ComponentModel();
        parseModel(mobj, model);
        model.setScheme(mobj.getString("scheme"));
        model.setExtendsScheme(mobj.getString("extendsScheme"));
        model.setAlternativeSchemes(mobj.getString("alternativeSchemes"));
        model.setSyntax(mobj.getString("syntax"));
        model.setAlternativeSyntax(mobj.getString("alternativeSyntax"));
        model.setAsync(mobj.getBooleanOrDefault("async", false));
        model.setConsumerOnly(mobj.getBooleanOrDefault("consumerOnly", false));
        model.setProducerOnly(mobj.getBooleanOrDefault("producerOnly", false));
        model.setLenientProperties(mobj.getBooleanOrDefault("lenientProperties", false));
        model.setGroupId(mobj.getString("groupId"));
        model.setArtifactId(mobj.getString("artifactId"));
        model.setVersion(mobj.getString("version"));
        JsonObject mcprp = (JsonObject) obj.get("componentProperties");
        for (Map.Entry<String, Object> entry : mcprp.entrySet()) {
            JsonObject mp = (JsonObject) entry.getValue();
            ComponentOptionModel option = new ComponentOptionModel();
            parseOption(mp, option, entry.getKey());
            model.addComponentOption(option);
        }
        JsonObject mprp = (JsonObject) obj.get("properties");
        for (Map.Entry<String, Object> entry : mprp.entrySet()) {
            JsonObject mp = (JsonObject) entry.getValue();
            EndpointOptionModel option = new EndpointOptionModel();
            parseOption(mp, option, entry.getKey());
            model.addEndpointOption(option);
        }
        return model;
    }

    public static String createParameterJsonSchema(ComponentModel model) {
        JsonObject obj = new JsonObject();
        obj.put("kind", model.getKind());
        obj.put("name", model.getName());
        obj.put("scheme", model.getScheme());
        obj.put("extendsScheme", model.getExtendsScheme());
        obj.put("alternativeSchemes", model.getAlternativeSchemes());
        obj.put("syntax", model.getSyntax());
        obj.put("alternativeSyntax", model.getAlternativeSyntax());
        obj.put("title", model.getTitle());
        obj.put("description", model.getDescription());
        obj.put("label", model.getLabel());
        obj.put("deprecated", model.isDeprecated());
        obj.put("deprecationNote", model.getDeprecationNote());
        obj.put("async", model.isAsync());
        obj.put("consumerOnly", model.isConsumerOnly());
        obj.put("producerOnly", model.isProducerOnly());
        obj.put("lenientProperties", model.isLenientProperties());
        obj.put("javaType", model.getJavaType());
        obj.put("firstVersion", model.getFirstVersion());
        obj.put("verifiers", model.getVerifiers());
        obj.put("groupId", model.getGroupId());
        obj.put("artifactId", model.getArtifactId());
        obj.put("version", model.getVersion());
        obj.entrySet().removeIf(e -> e.getValue() == null);
        JsonObject wrapper = new JsonObject();
        wrapper.put("component", obj);
        wrapper.put("componentProperties", asJsonObject(model.getComponentOptions()));
        wrapper.put("properties", asJsonObject(model.getEndpointOptions()));
        return Jsoner.prettyPrint(Jsoner.serialize(wrapper), 2, 2);
    }

    public ComponentModel() {
        setKind("component");
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getExtendsScheme() {
        return extendsScheme;
    }

    public void setExtendsScheme(String extendsScheme) {
        this.extendsScheme = extendsScheme;
    }

    public String getAlternativeSchemes() {
        return alternativeSchemes;
    }

    public void setAlternativeSchemes(String alternativeSchemes) {
        this.alternativeSchemes = alternativeSchemes;
    }

    public String getSyntax() {
        return syntax;
    }

    public void setSyntax(String syntax) {
        this.syntax = syntax;
    }

    public String getAlternativeSyntax() {
        return alternativeSyntax;
    }

    public void setAlternativeSyntax(String alternativeSyntax) {
        this.alternativeSyntax = alternativeSyntax;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isConsumerOnly() {
        return consumerOnly;
    }

    public void setConsumerOnly(boolean consumerOnly) {
        this.consumerOnly = consumerOnly;
    }

    public boolean isProducerOnly() {
        return producerOnly;
    }

    public void setProducerOnly(boolean producerOnly) {
        this.producerOnly = producerOnly;
    }

    public boolean isLenientProperties() {
        return lenientProperties;
    }

    public void setLenientProperties(boolean lenientProperties) {
        this.lenientProperties = lenientProperties;
    }

    public String getVerifiers() {
        return verifiers;
    }

    public void setVerifiers(String verifiers) {
        this.verifiers = verifiers;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<ComponentOptionModel> getComponentOptions() {
        return super.getOptions();
    }

    public void addComponentOption(ComponentOptionModel option) {
        super.addOption(option);
    }

    public List<EndpointOptionModel> getEndpointOptions() {
        return endpointOptions;
    }

    public void addEndpointOption(EndpointOptionModel option) {
        endpointOptions.add(option);
    }

    public List<EndpointOptionModel> getEndpointParameterOptions() {
        return endpointOptions.stream()
                .filter(o -> "parameter".equals(o.getKind()))
                .collect(Collectors.toList());
    }

    public List<EndpointOptionModel> getEndpointPathOptions() {
        return endpointOptions.stream()
                .filter(o -> "path".equals(o.getKind()))
                .collect(Collectors.toList());
    }

    public static class ComponentOptionModel extends BaseOptionModel {

    }

    public static class EndpointOptionModel extends BaseOptionModel {

    }
}
