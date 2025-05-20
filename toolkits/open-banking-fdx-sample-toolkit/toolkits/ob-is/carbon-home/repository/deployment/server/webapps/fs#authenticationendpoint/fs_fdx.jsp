<%--
 ~ Copyright (c) 2025, WSO2 LLC. (https://www.wso2.com).
 ~
 ~ WSO2 LLC. licenses this file to you under the Apache License,
 ~ Version 2.0 (the "License"); you may not use this file except
 ~ in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing,
 ~ software distributed under the License is distributed on an
 ~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 ~ KIND, either express or implied. See the License for the
 ~ specific language governing permissions and limitations
 ~ under the License.
 --%>

<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>

<%@ page import ="org.wso2.financial.services.fdx.consent.util.FDXConsentExtensionUtil"%>

<%
    session.setAttribute("consent_type", FDXConsentExtensionUtil.getAttribute(request, session, "consent_type", null));
    session.setAttribute("accounts_data", FDXConsentExtensionUtil.getAttribute(request, session, "account_data", null));
    session.setAttribute("redirectURL", FDXConsentExtensionUtil.getAttribute(request, session, "redirect_uri", null));
    session.setAttribute("consent_expiration", FDXConsentExtensionUtil.getAttribute(request, session, "consent_expiration", null));
    session.setAttribute("account_masking_enabled", FDXConsentExtensionUtil.getAttribute(request, session, "account_masking_enabled", null));
    session.setAttribute("app", FDXConsentExtensionUtil.getAttribute(request, session, "app", null));
    session.setAttribute("configParamsMap", FDXConsentExtensionUtil.getAttribute(request, session, "data_requested", null));
    session.setAttribute("newConfigParamsMap", FDXConsentExtensionUtil.getAttribute(request, session, "new_data_requested", null));
    session.setAttribute("sharing_duration_value", FDXConsentExtensionUtil.getAttribute(request, session, "sharing_duration_value", null));

%>

<html>
    <head>
        <jsp:include page="includes/head.jsp"/>
        <script>
            // Confirm sharing data
            function approvedFDX() {
                document.getElementById('consent').value = true;
                validateFDXFrm();
            }

            // Submit data sharing from
            function validateFDXFrm() {
                if (document.getElementById('type').value == "FDX_ACCOUNT") {
                    if (document.getElementById('account').value === "" ||
                        document.getElementById('account').value === "default") {
                        $(".acc-err").show();
                        return false;
                    } else {
                        console.log("Hello");
                        setTimeout(() => {  console.log("World!"); }, 2000);
                        document.getElementById("oauth2_authz_confirm").submit();
                    }
                }

                if (document.getElementById('type').value == "payments") {
                    if (document.getElementById('paymentAccount').value === "" ||
                        document.getElementById('paymentAccount').value === "default") {
                        $(".acc-err").show();
                        return false;
                    } else {
                        document.getElementById("oauth2_authz_confirm").submit();
                    }
                }

                if (document.getElementById('type').value == "fundsconfirmations") {
                    document.getElementById("oauth2_authz_confirm").submit();
                }
            }
        </script>
    </head>

    <body>
        <div class="page-content-wrapper" style="position: relative; min-height: 100vh;">
            <div class="container-fluid " style="padding-bottom: 40px">
                <div class="container">
                    <div class="login-form-wrapper">
                        <div class="row">
                            <img src="images/logo-dark.svg"
                                 class="img-responsive brand-spacer login-logo" alt="WSO2 Open Banking"/>
                        </div>
                        <div class="row data-container">
                            <%
                                session.setAttribute("configParamsMap", request.getAttribute("data_requested"));
                                Map<String, List<String>> consentData = (Map<String, List<String>>) request.getAttribute("data_requested");
                            %>

                            <div class="clearfix"></div>
                            <form action="${pageContext.request.contextPath}/fs_fdx_account_selection.do" method="post" id="fdx_consent_display"
                                  name="fdx_consent_display" class="form-horizontal">
                                <div class="login-form">
                                    <div class="form-group ui form">
                                        <div class="col-md-12 ui box">
                                        	<h3 class="ui header">

                                                <%-- Change heading based on the consent type --%>
                                                <c:choose>
                                                    <c:when test="${consent_type eq 'FDX_ACCOUNT'}">
                                                        <strong>${app}</strong> requests account details on your account.
                                                    </c:when>
                                                </c:choose>
                                            </h3>

                                            <h4 class="section-heading-5 ui subheading">Data requested:</h4>
                                            <!--Display requested data-->
                                            <c:forEach items="<%=consentData%>" var="record">
                                                <div class="padding" style="border:1px solid #555;">
                                                    <b>${record.key}</b>
                                                    <ul class="scopes-list padding">
                                                        <c:forEach items="${record.value}" var="record_data">
                                                            <li>${record_data}</li>
                                                        </c:forEach>
                                                    </ul>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>

                                    <div class="form-group ui form">
                                        <div class="col-md-12 ui box">
                                            <c:choose>
                                                <c:when test="${durationPeriod == 0}">
                                                    ${app} requests one-time access to your data. Access will be removed after the transaction is completed.
                                                </c:when>
                                                <c:otherwise>
                                                    ${app} requests access to your data for the next ${durationPeriod} days expiring on ${ExpirationDateTime}.
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="form-group ui form">
                                        <div class="col-md-12 ui box">
                                            If you want to stop sharing data, you can request us to stop sharing data on your data sharing
                                            dashboard.
                                            </br>
                                            Do you confirm that we can share your data with ${app}?
                                        </div>
                                    </div>

                                    <div class="form-group ui form row">
                                        <div class="ui body col-md-12">
                                            <input class="ui default column button btn btn-default" type="reset" value="Deny" onclick="showModal()"
                                                data-toggle="modal" data-target="#cancelModal" />
                                            <input type="button" class="ui default column button btn btn-default" id="back" name="back"
                                                onclick="history.back();" value="Back"/>
                                            <input type="submit" class="ui primary column button btn" id="approve" name="approve"
                                                value="Continue"/>
                                            <input type="hidden" id="hasApprovedAlways" name="hasApprovedAlways" value="false"/>
                                            <input type="hidden" name="sessionDataKeyConsent" value="${sessionDataKeyConsent}"/>
                                            <input type="hidden" name="consent" id="consent" value="deny"/>
                                            <input type="hidden" name="app" id="app" value="${app}"/>
                                            <input type="hidden" name="type" id="type" value="${consent_type}"/>
                                            <input type="hidden" name="accounts[]" id="account" value="">
                                            <input type="hidden" name="accNames" id="accountName" value=""/>
                                        </div>
                                    </div>

                                    <div class="form-group ui form row">
                                        <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                            <div class="well policy-info-message" role="alert margin-top-5x">
                                                <div>
                                                    ${privacyDescription}
                                                    <a href="privacy_policy.do" target="policy-pane">
                                                        ${privacyGeneral}
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Cancel Modal -->
            <div class="modal" id="cancelModal" style="display:none; overflow: auto;">
                <div class="modal-dialog">
                    <div class="modal-content">

                        <!-- Modal body -->
                        <div class="modal-body">
                            <p style="color:black"> Unless you confirm your authorisation, we won't be able to share your data with
                                "${app}". <br>
                                <br> Are you sure you would like to cancel this process? </p>

                            <div class="ui two column grid">
                                <table style="width:100%">
                                    <tbody>
                                    <tr>
                                        <td>
                                            <div class="md-col-6 column align-left buttons">
                                                <input type="button" onclick="redirect()" class="ui default column button btn btn-default"
                                                       id="registerLink" role="button" value="Yes cancel">
                                            </div>
                                        </td>
                                        <td>
                                            <div class="column align-right buttons">
                                                <input type="button" onclick="closeModal()" class="ui primary column button btn" role="button"
                                                       value="No continue" style="float:right;">
                                            </div>
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <jsp:include page="includes/footer.jsp"/>
        </div>
        <script>

            let modal = document.getElementById("cancelModal");

            function showModal() {
                modal.style.display = "block";
            }

            function closeModal() {
                modal.style.display = "none";
                document.body.style.overflow = "auto";
                document.body.style.backgroundColor = "";
            }

            function redirect() {
                console.log("Redirecting to the cancel page");
                let error = "User skip the consent flow";
                console.log("Error: " + error);
                let redirectURL = "${redirectURL}";
                console.log("Redirect URL: " + redirectURL);

                top.location = "${redirectURL}#error=access_denied&error_description=" + error;
            }
        </script>
    </body>
</html>