<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>


<div class="page-content">
    <form action="${paypalAction}" method="post">
	    <input type="hidden" name="cmd" value="_s-xclick"> <input
	        type="hidden" name="hosted_button_id" value="${hostedButtonID}">
	    <input type="image"
	        src="https://www.sandbox.paypal.com/it_IT/i/btn/btn_buynowCC_LG.gif"
	        border="0" name="submit"
	        alt="PayPal - The safer, easier way to pay online!"> <img
	        alt="" border="0"
	        src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1"
	        height="1"> <input type="hidden" name="return"
	        value="${returnUrl}"> <input type="hidden" name="custom"
	        value="${email}">
	</form>
</div>
