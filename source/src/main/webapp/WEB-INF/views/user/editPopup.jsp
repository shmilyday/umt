<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<div id="edit-popup" data-backdrop="true" tabindex="-1"
	class="modal hide fade smallBottom" style="width: 750px;">
	<div class="modal-header">
		<button type="button" class="close" data-dismiss="modal">×</button>
		<h3>
			<span id="modalTitle"></span>
			<fmt:message key="develop.oauth.modify.title" />
		</h3>
	</div>
	<form method="post" id="editBasic" name="edit-baseinfo"
		class="form-horizontal no-bmargin" style="margin: 0"
		action="<umt:url value='/user/developer.do'/>">
		<fieldset>
			<div class="modal-body">
				<input type="hidden" id="id" name="id" value="0" /> <input
					type="hidden" id="act" name="act" value="addOauth" /> <input
					type="hidden" id="thirdParty" name="thirdParty" value="" />
				<div class="control-group">
					<label class="control-label"><fmt:message
							key="develop.oauth.modify.clientName" /></label>
					<div class="controls">
						<input maxlength="254" type="text" name="clientName"
							id="clientName" class="register-xlarge" /> <span
							class="error help-inline" id="clientName_error"></span>
					</div>
				</div>
				<div class="control-group" id="appTypeControl">
					<label class="control-label"><fmt:message
							key='develop.oauth.modify.appType' /> <fmt:message
							key='common.maohao' /></label>
					<div class="controls">
						<label style="display: inline;" for="appTypeWeb"> <input
							type="radio" name="appType" checked="checked" id="appTypeWeb"
							value="webapp" /> <fmt:message
								key='develop.oauth.modify.appType.webapp' />
						</label> <label style="display: inline;" for="appTypePhone"> <input
							type="radio" name="appType" id="appTypePhone" value="phoneapp" />
							<fmt:message key='develop.oauth.modify.appType.phoneapp' />
						</label> <label style="display: inline;" for="appTypeLdap"
							id="ldapAppType"> <input type="radio" name="appType"
							id="appTypeLdap" value="ldap" /> <fmt:message
								key='develop.oauth.modify.appType.ldap' />
						</label> <label style="display: inline;" for="appTypeWifi"
							id="ldapAppType"> <input type="radio" name="appType"
							id="appTypeWifi" value="wifi" /> <fmt:message
								key='develop.oauth.modify.appType.wifi' />
						</label> <span class="error help-inline" id="appType_error"></span>
					</div>
				</div>
				<div class="control-group ldapSpan"
					style="margin-bottom: 10px; display: none">
					<label class="control-label"><fmt:message
							key='develop.ldap.app.code' /> <fmt:message key='common.maohao' /></label>
					<div class="controls">
						<input maxlength="250" type="text" name="rdn" id="rdn"
							class="register-xlarge" /> <span id="rdnDisplay"></span> <span
							class="error help-inline" id="rdn_error"></span>
						<p id="rdnHint" class="help-inline">
							<fmt:message key='develop.ldap.app.code.hint' />
						</p>
					</div>
				</div>
				<div class="control-group wifiSpan"
					style="margin-bottom: 10px; display: none">
					<label class="control-label"><fmt:message
							key='develop.ldap.pubScope' /> <fmt:message key='common.maohao' /></label>
					<div class="controls">
						<input type="hidden" name="publishToAll" value="false" />
						<fmt:message key='develop.ldap.pubScope.toSpecial' />
						<input maxlength="250" type="text" name="pubScope" id="pubScope"
							class="register-xlarge publishToSpecial" /> <span
							id="pubScopeDisplay"></span> <span
							class="error help-inline publishToSpecial" id="pubScope_error"></span>
					</div>
				</div>
				<div class="control-group oauthSpan">
					<label class="control-label"><fmt:message
							key='app.access.pwd'></fmt:message> <fmt:message
							key="common.maohao" /></label>
					<div class="controls">
						<label style="display: inline;"> <input type="radio"
							name="enableAppPwd" value="yes" /> <fmt:message
								key="app.access.pwd.use"></fmt:message>
						</label> <label style="display: inline;"> <input type="radio"
							name="enableAppPwd" checked="checked" value="no" /> <fmt:message
								key="app.access.pwd.unuse"></fmt:message>
						</label>
					</div>
				</div>
				<div class="control-group ldapSpan">
					<label class="control-label"><fmt:message
							key='develop.ldap.permision' /> <fmt:message key='common.maohao' /></label>
					<div class="controls">
						<label style="display: inline;"> <input type="radio"
							name="priv" value="open" /> <fmt:message
								key='develop.ldap.permision.open' />
						</label> <label style="display: inline;"> <input type="radio"
							name="priv" value="needApply" /> <fmt:message
								key='develop.ldap.permision.needApply' />
						</label> <label style="display: inline;" id="closedPrivSpan"> <input
							type="radio" name="priv" value="closed" /> <fmt:message
								key='develop.ldap.permision.closed' /> <fmt:message
								key='develop.ldap.permision.closed.hint' />
						</label>
					</div>
				</div>
				<div class="control-group oauthSpan">
					<label class="control-label"><fmt:message
							key="develop.oauth.modify.website" /></label>
					<div class="controls">
						<input maxlength="250" type="text" name="clientWebsite"
							id="clientWebsite" class="register-xlarge" /> <span
							class="error help-inline" id="clientWebsite_error"></span>
						<p class="help-inline">
							<fmt:message key='develop.oauth.hint.website' />
						</p>
					</div>
				</div>
				<div class="control-group oauthSpan">
					<label class="control-label"><fmt:message
							key="develop.oauth.redirect.url" /></label>
					<div class="controls">
						<input maxlength="250" type="text" name="redirectURI"
							id="redirectURI" class="register-xlarge" /> <span
							class="error help-inline" id="redirectURI_error"></span>
						<p class="help-inline">
							<fmt:message key='develop.oauth.hint.callbak' />
						</p>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"><fmt:message
							key="develop.oauth.description" /></label>
					<div class="controls">
						<textarea name="description" id="description"
							style="width: 25em; height: 3em; resize: none;"></textarea>
						<span class="error help-inline" id="description_error"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"><fmt:message
							key="develop.oauth.modify.applicant" /></label>
					<div class="controls">
						<input name="applicant" type="text" id="applicant"
							class="register-xlarge" /> <span class="error help-inline"
							id="applicant_error"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"><fmt:message
							key="develop.oauth.modify.company" /></label>
					<div class="controls">
						<input name="company" type="text" id="company"
							class="register-xlarge" /> <span class="error help-inline"
							id="company_error"></span>
					</div>
				</div>
				<div class="control-group">
					<label class="control-label"><fmt:message
							key="develop.oauth.modify.contactInfo" /></label>
					<div class="controls">
						<input name="contactInfo" type="text" id="contactInfo"
							class="register-xlarge" /> <span class="error help-inline"
							id="contactInfo_error"></span>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<a data-dismiss="modal" class="btn" href="#"><fmt:message
						key="inputpassword.cancel" /></a>
				<button type="submit" class="btn btn-primary">
					<fmt:message key="userinfo.submit" />
				</button>
			</div>
		</fieldset>
	</form>
</div>
<script type="text/javascript">
	function customValidateMethod() {
		//验证长度 
		$.validator.addMethod("startWith", function(value, element, params) {
			var reg = new RegExp("^" + $('#' + params).val());
			return reg.test(value);
		}, "<fmt:message key='develop.oauth.validate.startWith'/>");

		$.validator.addMethod("rdnRegex", function(value, element, params) {
			var regix = /^[a-zA-Z0-9_\-]+$/;
			return regix.test(value);
		}, '<fmt:message key="develop.ldap.app.validate"/>');

		$.validator.addMethod("pubScope", function(value, element) {
			if ($('input[name=appType]:checked').val()=='wifi'){
				if (value == null || value == '') {
					return false;
				}
				var domains = value.split(";");
				var pattern = /^([0-9a-z\-]+\.)+[0-9a-z]+$/;
				var result = true;
				$.each(domains, function(index, domain) {
					if (!pattern.test(domain)) {
						result = false;
					}
				});
				return result;
			}else{
				return true;
			}
		}, '<fmt:message key="develop.oauth.modify.ladp.pubScop"/>');
	}
	function setFormValidator() {
		//form validator
		$('#editBasic')
				.validate(
						{
							submitHandler : function(form) {
								var appType = $('input[name=appType]:checked')
										.val();
								if (appType == 'ldap' || appType == 'wifi') {
									var url;
									if ($('#id').val() == '0') {
										url = '${context}/user/developer.do?act=addLdapApp';
									} else {
										url = '${context}/user/developer.do?act=updateLdapApp';
									}
									$
											.post(url, $(form).serialize())
											.done(
													function() {
														if (appType == 'ldap') {
															window.location.href = "${context}/user/developer.do?act=display&viewType=ldap";
														} else {
															window.location.href = "${context}/user/developer.do?act=display&viewType=wifi";
														}
													});
								} else {
									form.submit();
								}
							},
							rules : {
								applicant : {
									required : true,
									maxlength : 45
								},
								clientName : {
									required : true,
									maxlength : 45
								},
								clientWebsite : {
									required : true,
									url : true,
									maxlength : 250
								},
								company : {
									required : true,
									maxlength : 25
								},
								rdn : {
									required : true,
									maxlength : 20,
									rdnRegex : true,
									remote : {
										type : "POST",
										url : '<umt:url value="/user/developer.do?act=isLdapAppNameUsed"/>'
									}
								},
								pubScope : {
									pubScope : true
								},
								contactInfo : {
									required : true,
									maxlength : 45
								},
								description : {
									required : true,
									maxlength : 500
								},
								redirectURI : {
									required : true,
									url : true,
									startWith : 'clientWebsite',
									maxlength : 250
								}
							},
							messages : {
								applicant : {
									required : '<fmt:message key="develop.oauth.validate.applicant.required"/>',
									maxlength : '<fmt:message key="develop.oauth.validate.content.too.long"/>'
								},
								pubScope : {
									required : '<fmt:message key="develop.oauth.validate.pubScope.required"/>'
								},
								clientName : {
									required : '<fmt:message key="develop.oauth.validate.clientName.required"/>',
									maxlength : '内容超长'
								},
								clientWebsite : {
									required : '<fmt:message key="develop.oauth.validate.website.required"/>',
									url : '<fmt:message key="develop.oauth.validate.url.format"/>',
									maxlength : '<fmt:message key="develop.oauth.validate.content.too.long"/>'
								},
								company : {
									required : '<fmt:message key="develop.oauth.validate.company.required"/>',
									maxlength : '<fmt:message key="develop.oauth.validate.content.too.long"/>'
								},
								rdn : {
									required : '<fmt:message key="develop.ldap.app.code.required"/>',
									maxlength : '<fmt:message key="develop.ldap.app.code.max"/>',
									remote : '<fmt:message key="develop.ldap.app.code.used"/>'
								},
								contactInfo : {
									required : '<fmt:message key="develop.oauth.validate.contactInfo.required"/>',
									maxlength : '<fmt:message key="develop.oauth.validate.content.too.long"/>'
								},
								description : {
									required : '<fmt:message key="develop.oauth.validate.description.required"/>',
									maxlength : '<fmt:message key="develop.oauth.validate.content.too.long"/>'
								},
								redirectURI : {
									required : '<fmt:message key="develop.oauth.validate.redirectUrl.required"/>',
									url : '<fmt:message key="develop.oauth.validate.url.format"/>',
									maxlength : '<fmt:message key="develop.oauth.validate.content.too.long"/>'
								}
							},
							errorPlacement : function(error, element) {
								var sub = "_error";
								var errorPlaceId = "#"
										+ $(element).attr("name") + sub;
								$(errorPlaceId).html("");
								error.appendTo($(errorPlaceId));
							},
							ignore : ":not(:visible)"
						});
	}
	function pubScopeClick() {
		if ($(":radio.publishToAll:checked").val() == 'true') {
			$(".publishToSpecial").hide();
			$("#pubScope").rules("remove", "required");
			$("#pubScope_error").html("");
			$("#pubScope").val("");
		} else {
			$(".publishToSpecial").show();
			$("#pubScope").rules("add", {
				required : true
			});
		}
	}
	function setBehavior() {
		$('input[name=appType]').on('click', function() {
			switch ($(this).val()) {
				case "wifi":{
					pubScopeClick();
					$('.oauthSpan').hide();
					$('.ldapSpan').show();
					$('.wifiSpan').show();
					$(':radio.publishToAll[value=false]').attr("checked", true);
					return;
				}
				case "ldap": {
					$('.oauthSpan').hide();
					$('.wifiSpan').hide();
					$('.ldapSpan').show();
					return;
				}
				default: {
					$('.oauthSpan').show();
					$('.ldapSpan').hide();
					$('.wifiSpan').hide();
					return;
				}
			}
		});
		$(":radio.publishToAll").on('click',function(){
			pubScopeClick();
		});
	}
	function showNewDialog(){
		$('#id').val('0');
		$('#applicant').val('');
		$('#clientName').val('');
		$('#clientWebsite').val('');
		$('#company').val('');
		$('#contactInfo').val('');
		$('#description').val('');
		$('#rdn').show();
		$('#rdnDisplay').hide();
		$('#rdnHint').show();
		$('#pubScope').val('');
		$('#redirectURI').val('');
		$('#closedPrivSpan').hide();
		$('#act').val('addOauth');
		$('input[name=priv][value=open]').click();
		$('input[name=appType][value=webapp]').click();
		$('input[name=enableAppPwd][value=no]').click();
		$('#thirdParty').val('');
		$('.error').html("");
		$('#ldapAppType').show();
		$('#appTypeControl').show();
		$('#modalTitle').html('<fmt:message key="develop.oauth.modify.title.add"/>');
		$('#edit-popup').modal('show');
	}
	$(document).ready(function() {
		customValidateMethod();
		setFormValidator();
		setBehavior();
	});
</script>