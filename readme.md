[White Source agents][1]
===================

Agents keep your [White Source][2] account up to date automatically.
They send a statement of the current use of open source software in your project to [White Source][2].

This information is used to:
 
 * Create and setup new projects
 * Update existing projects inventories
    * Open requests for newly introduced libraries
    * Cancel requests for removed libraries.

An agent is a plugin for one or more components in the your project ecosystem. Build systems, continuous integration servers, etc...

### Getting started
Setup and usage instructions for each agent can be found in the [documentation][1].
We recommend to visit the [service documentation][7] as well.

### Available plugins
At the moment we have plugins for [Maven][3], [Ant][10], [Jenkins][4] and [TeamCity][5].
More plugins will be available soon. 
Meanwhile, if you need a plugin for another system please drop a line to our [support team][6].

### Developers
Agents are clients to the service REST api. You can develop your own agent by implementing such a client.
This project provide two modules for Java based agents:

 * Domain model for information and operations in the api
 * Client implementation of the api
 
More information can be found in the [documentation][1]. 
You can have a look in the [technical information][9] if you didn't find what you need.

### Support
You can always create an issue or tell our [support team][6] what you think.

### License
The project is licensed under the [Apache 2.0][8] license.
<pre>
Copyright (C) 2012 White Source Ltd.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
</pre>

[1]: http://docs.whitesourcesoftware.com/display/docs/Agents
[2]: http://www.whitesourcesoftware.com
[3]: http://www.github.com/whitesource/maven-plugin
[4]: http://www.github.com/whitesource/jenkins-whitesource-plugin
[5]: http://www.github.com/whitesource/teamcity-plugin
[6]: mailto:support@whitesourcesoftware.com
[7]: http://docs.whitesourcesoftware.com/display/serviceDocs/Home
[8]: http://www.apache.org/licenses/LICENSE-2.0.html
[9]: http://whitesource.github.com/agents
[10]: http://www.github.com/whitesource/ant-plugin
