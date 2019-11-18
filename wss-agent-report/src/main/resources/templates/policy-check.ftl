<#--
* Copyright (C) 2012 White Source Ltd.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
-->
<#macro reject> <span class='wssRed'>Policy Violations Found</span> </#macro>
<#macro approve> <span class='wssGreen'>No Policy Violations</span> </#macro>
<#macro rejectSingle(policyName)>	<div style="float: right;">
    <div class='policy policyReject' title="Rejected by policy '${policyName}'">Reject</div>
    <span class='wssAnchor' title="Rejected by policy '${policyName}'">info</span>
</div>
</#macro>
<#macro approveSingle(policyName)><div style="float: right;">
    <div class='policy policyApprove' title="Approved by policy '${policyName}'">Approve</div>
    <span class='wssAnchor' title="Approved by policy '${policyName}'">info</span>
</div>
</#macro>
<#macro reassignSingle(policyName)><div style="float: right;">
    <div class='policy policyReassign' title="The request for this library will be reassigned by policy '${policyName}'">Reassign</div>
    <span class='wssAnchor' title="The request for this library will be reassigned by policy '${policyName}'">info</span>
</div>
</#macro>
<#macro conditionSingle(policyName)><div style="float: right;">
    <div class='policy policyConditions' title="Conditions will be created by policy '${policyName}'">Conditions</div>
    <span class='wssAnchor' title="Conditions will be created by policy '${policyName}'">info</span>
</div>
</#macro>
<#macro issueSingle(policyName)><div style="float: right;">
    <div class='policy policyIssue' title="An issue will be created by policy '${policyName}'">Issue</div>
    <span class='wssAnchor' title="An issue will be created by policy '${policyName}'">info</span>
</div>
</#macro>
<#macro lineSeparator> <div class='wssLineSeparaterContainer'><div class='wssLineSeparator'></div></div> </#macro>
<#macro projectsSummary(projects, detailsPrefix)>  <#list projects?keys as entry><#assign detailsId = detailsPrefix + "-details-" + entry?counter>
<div class='wssProjectEntry'>
    <#-- project caption
    -->            <div style="width: 100%;" class='wssBorder wssProjectCaptionBackground'>
    <div style="display: inline-block;">
        <table style="width: 100%;">
            <tr>
                <td>
                    <div style="padding: 5px;">${entry}</div>
                </td>
            </tr>
        </table>
    </div><div style="display: inline-block; float: right;">
<table style="width: 100%;">
<tr>
<td>
<div style="display: inline-block;">
    <#if projects[entry].hasRejections()>                                        <@reject/>                                    <#else>                                        <@approve/>                                    </#if>                                </div>
    </td><td align="right">
        <a class='wssAnchor' href="#" onclick="toggleDetails(this, '${detailsId}')" style="float: right; padding: 5px;">show details</a>
    </td>
    </tr>
    </table>
</div>
</div>

<#-- project details
-->            <div id="${detailsId}" style="width: 100%; display: none;" class='wssBorder wssDetailsBackground'>
    <@projectDependenciesTree projects[entry]/>
</div>
</div>

</#list></#macro>
<#macro projectDependenciesTree(root)>    <#if (root.children?size>0)>        <ul>
    <#foreach child in root.children>                <@dependencyNode child/>
</#foreach>        </ul>
<#else>    <div style="padding: 10px;">No new libraries were detected in this project</div>
</#if></#macro>
<#macro dependencyNode(node)>    <li>
<div class="wssDependencyNode">
    <a class='wssAnchor' href="${node.resource.link}" target="_blank">${node.resource.displayName}</a>
<div class="wssTextColor" style="display: inline; padding-left: 20px;">
    <#foreach license in node.resource.licenses>                ${license} &nbsp;
</#foreach>			</div>

    <#if node.policy ??>                <#if "Reject" == node.policy.actionType>                    <@rejectSingle node.policy.displayName/>                <#elseif "Approve" == node.policy.actionType>                    <@approveSingle node.policy.displayName/>                <#elseif "Reassign" == node.policy.actionType>                    <@reassignSingle node.policy.displayName/>                <#elseif "Conditions" == node.policy.actionType>                    <@conditionSingle node.policy.displayName/>                <#elseif "Issue" == node.policy.actionType>                    <@issueSingle node.policy.displayName/>                </#if>            </#if>        </div>

<#if node.children ??>            <ul>
    <#foreach child in node.children>                <@dependencyNode child/>
</#foreach>            </ul>
</#if>    </li>
</#macro>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>WhiteSource - Policy Check Summary</title>
    <meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type" />

    <script>
        function toggleDetails(anchor, detailsId) {
            var details = document.getElementById(detailsId);
            if (details.style.display == 'block') {
                details.style.display = 'none';
                anchor.innerHTML = 'show details';
            } else {
                details.style.display = 'block';
                anchor.innerHTML = 'hide details';
            }
            return details;
        }
    </script>

    <style>
        .wssBody {
            font-family: verdana;
            font-size: 14px;
        }

        .wssContainer {
            width: 750px;
            padding-bottom: 10px;
            border: 5px solid #3E68C6;
        }

        .wssTextColor {
            color: #4B4A4A; 
        }

        .wssHeader {
            color: #3E68C6;
            font-weight: bold;
        }

        .wssFont {
            font-family: verdana;
        }

        .wssTableHeader {
            font-weight: bold;
            color: #3E68C6;
            text-align: left;
        }
        .wssTable {
            border: 1px solid #d1d1d1;
            background-color: white;
            padding: 5px;
            vertical-align: top;
            border-spacing: 0px 2px;
        }

        .wssAnchor {
            color: #3E68C6;
            text-decoration: underline;
        }

        .wssAnchor:link {
            color: #3E68C6;
        }

        .wssAnchor:visited {
            color: #3E68C6;
        }

        .policy {
            width: 65px;
            padding: 2px 5px 2px 5px;
            border-radius: 1px;
            text-align: center;
            font-size: 12px;
            display: inline-block;
        }

        .policyApprove {
            background: #7fb611;
            border: 1px solid #6f9f0f;
            color: white;
        }

        .policyReject {
            background: #d1260f;
            color: white;
            border: 1px solid darkRed;
        }

        .policyReassign {
            background: #ffe438;
            border: 1px solid #ebcb00;
            color: #252b35;
        }

        .policyConditions {
            background: #ff8138;
            border: 1px solid #df752b;
            color: white;
        }

        .policyIssue {
            background: #243a77;
            border: 1px solid #243a77;
            color: white;
        }

        .wssRed {
            color: #D1260F;
        }

        .wssGreen {
            color: #76A321;
        }

        .wssBorder {
            border: 1px solid #d1d1d1;
        }

        .wssDetailsBackground {
            background: #FAFAFA;
        }

        .wssProjectCaptionBackground {
            background: white;
        }

        .wssTextAlign {
            text-align: left;
        }

        .wssProjectHeader {
            margin: 5px 0 0 4px;
            font-size: 120%;
            color: #727272;
            font-weight: bold;
            position: relative;
            top: -4px;
        }

        .wssProjectTitle {
            font-weight: bold;
            margin: 10px 0 10px 0px;
        }

        .wssProjectStats {
            position: relative;
            top: -4px;
        }

        .wssProjectEntry {
            margin: -1px 10px 0px 10px;
        }

        .wssLineSeparator {
            height: 1px;
            background: lightGrey;
        }

        .wssLineSeparaterContainer {
            margin: 0px 10px;
            padding-top: 10px;
            padding-bottom: 10px;
        }

        .wssLicenseBar {
            border-spacing: 0px 0px;
            background: #3E68C6;
            width: 20px;
            border-radius: 2px 2px 0px 0px;
        }

        .wssLicenses {
            border-spacing: 0px 0px;
            width:100%;
            height: 115px;
            border-bottom: 1px solid #ccc;
            font-size: 12px;
            background: #FAFAFA;
        }

        .wssDependencyNode {
            padding-bottom: 4px;
            padding-top: 4px;
            margin-right: 10px;
        }
    </style
</head>

<body class='wssBody'>
<div align="center" style="width: 100%;">
<div class='wssContainer'>
    <a class='wssAnchor' href="https://whitesourcesoftware.com" target="_blank">
        <img align="center" src="https://saas.whitesourcesoftware.com/Wss/background/whitesource_logo_new_dark_large.png" style="padding-top: 10px;"/>
    </a>

<div align="center">
    <h2 class='wssHeader'>Policy Check Summary</h2>
<h3>
    <#if .data_model["hasRejections"]>                    <@reject/>                <#else>                    <@approve/>                </#if>            </h3>
</div>

<div align='left'>
<div class='wssTextAlign' style="width: 500px; padding-left: 10px;">
<table style="width: 100%;" class='wssTextColor'>
<tbody>
<#if .data_model["buildName"] ??>                        <div class='wssTextColor' style="padding-bottom: 10px; padding-left: 5px;">
    Build Name - ${.data_model["buildName"]}
</div>
</#if>	                    <#if .data_model["buildNumber"] ??>                        <div class='wssTextColor' style="padding-bottom: 10px; padding-left: 5px;">
    Build Number - ${.data_model["buildNumber"]}
</div>
</#if>                        <div class='wssTextColor' style="padding-left: 5px;">
    Report creation time - ${.data_model["creationTime"]}
</div>
    </tbody>
    </table>
</div>
</div>

    <@lineSeparator/>
<div class='wssTextAlign'>
    <#if (result.newProjects?size>0)>                <div style='padding-bottom: 5px; padding-left: 10px;'>
    <div class='wssProjectHeader' style="display: inline-block;">New Projects</div>
    <span class='wssTextColor wssProjectStats'>(found ${result.newProjects?size} new projects)</span>
</div>
    <@projectsSummary result.newProjects "new-project"/>            <#else>                <div class='wssTextColor wssProjectStats' style="padding-left: 15px; padding-top: 5px;">No new projects found</div>
</#if>
    <@lineSeparator/>
    <#if (result.existingProjects?size>0)>                <div style='padding-bottom: 5px; padding-left: 10px;'>
    <div class='wssProjectHeader' style="display: inline-block;">Existing Projects</div>
    <span class='wssTextColor wssProjectStats'>(found ${result.existingProjects?size} existing projects)</span>
</div>
    <@projectsSummary result.existingProjects "existing-project"/>            <#else>                <div class='wssTextColor wssProjectStats' style="padding-left: 15px; padding-top: 5px;">No existing projects were updated</div>
</#if>        </div>


<#if (licenses?size>0)>
<@lineSeparator/>
<div class='wssTextAlign' style="margin: 0px 10px;">
    <div>
        <div class='wssProjectHeader' style="display: inline-block">License Distribution</div>
    </div>
<table class="wssLicenses wssTextColor">
<tr>
    <#foreach license in licenses>		      		<td style='width: 100px; padding: 0px; vertical-align: bottom;'>
    <table style="border-spacing: 0px 0px; width: 100px; text-align: center">
        <tr>
            <td style=' font-size:12px;'>
                ${license.occurrences}
            <td>
        </tr>
        <tr>
            <td style="padding-bottom: 0px; padding-right: 40px; padding-left: 40px">
                <table height="${license.height}" class="wssLicenseBar" title="${license.name}: ${license.occurrences}">
                    <tr>
                        <td>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</td>
</#foreach>		  		</tr>
</table>
<table style='width: 100%;'>
<tr>
    <#foreach license in licenses>		    		<td>
    <table style='width: 100px;'>
        <tr>
            <td class="wssTextColor" style='font-size: 80%; text-align: center;' title="${license.name}">
                ${license.shortName}
            </td>
        </tr>
    </table>
</td>
</#foreach>				</tr>
</table>
</div>
</#if>
</div>
</div>
</body>
</html>
