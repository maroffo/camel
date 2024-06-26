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
package org.apache.camel.component.activemq;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Category;
import org.apache.camel.component.jms.JmsBinding;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.spi.EndpointServiceLocation;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;

/**
 * Send messages to (or consume from) Apache ActiveMQ 5.x. This component extends the Camel JMS component.
 */
@UriEndpoint(firstVersion = "1.0.0", extendsScheme = "jms", scheme = "activemq", title = "ActiveMQ 5.x",
             syntax = "activemq:destinationType:destinationName",
             category = { Category.MESSAGING })
public class ActiveMQEndpoint extends JmsEndpoint implements EndpointServiceLocation {

    @UriParam(multiValue = true, prefix = "destination.", label = "consumer,advanced")
    private Map<String, String> destinationOptions;

    public ActiveMQEndpoint() {
    }

    public ActiveMQEndpoint(String uri, JmsComponent component, String destinationName, boolean pubSubDomain,
                            JmsConfiguration configuration) {
        super(uri, component, destinationName, pubSubDomain, configuration);
    }

    public ActiveMQEndpoint(String endpointUri, JmsBinding binding, JmsConfiguration configuration, String destinationName,
                            boolean pubSubDomain) {
        super(endpointUri, binding, configuration, destinationName, pubSubDomain);
    }

    public ActiveMQEndpoint(String endpointUri, String destinationName, boolean pubSubDomain) {
        super(endpointUri, destinationName, pubSubDomain);
    }

    public ActiveMQEndpoint(String endpointUri, String destinationName) {
        super(endpointUri, destinationName);
    }

    @Override
    public String getServiceUrl() {
        String url = null;
        if (getConfiguration() instanceof ActiveMQConfiguration acc) {
            url = acc.getBrokerURL();
        }
        if (url == null && getComponent() instanceof ActiveMQComponent amq) {
            url = amq.getBrokerURL();
        }
        return url;
    }

    @Override
    public String getServiceProtocol() {
        return "jms";
    }

    @Override
    public Map<String, String> getServiceMetadata() {
        String un = null;
        String cid = null;
        if (getConfiguration() instanceof ActiveMQConfiguration acc) {
            un = acc.getUsername();
            cid = acc.getClientId();
        }
        if (un == null) {
            un = getComponent().getUsername();
        }
        if (cid == null) {
            cid = getClientId();
        }
        if (un == null && cid == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        if (un != null) {
            map.put("username", un);
        }
        if (cid != null) {
            map.put("clientId", cid);
        }
        return map;
    }

    public Map<String, String> getDestinationOptions() {
        return destinationOptions;
    }

    /**
     * Destination Options are a way to provide extended configuration options to a JMS consumer without having to
     * extend the JMS API. The options are encoded using URL query syntax in the destination name that the consumer is
     * created on. See more details at https://activemq.apache.org/destination-options.
     */
    public void setDestinationOptions(Map<String, String> destinationOptions) {
        this.destinationOptions = destinationOptions;
    }
}
