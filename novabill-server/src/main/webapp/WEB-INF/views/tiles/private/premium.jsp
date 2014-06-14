<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>


<div class="page-content">

    <form action="${paypalAction}" method="post" target="_top">
		<input type="hidden" name="cmd" value="_s-xclick">
		<input type="hidden" name="hosted_button_id" value="5UMNZ2VLVRAP4">
		<table>
		<tr><td><input type="hidden" name="on0" value="Numero di anni">Numero di anni</td></tr><tr><td><select name="os0">
		    <option value="1 anno">1 anno €60,00 EUR</option>
		    <option value="2 anni (sconto 10%)">2 anni (sconto 10%) €108,00 EUR</option>
		</select> </td></tr>
		</table>
		<input type="hidden" name="currency_code" value="EUR">
		<input type="image" src="https://www.paypalobjects.com/it_IT/IT/i/btn/btn_buynowCC_LG.gif" border="0" name="submit" alt="PayPal - Il metodo rapido, affidabile e innovativo per pagare e farsi pagare.">
		<img alt="" border="0" src="https://www.paypalobjects.com/it_IT/i/scr/pixel.gif" width="1" height="1">
	</form>
		    






    <form action="${paypalAction}" method="post">
	    <input type="hidden" name="cmd" value="_s-xclick">
	    <input type="hidden" name="hosted_button_id" value="${hostedButtonIDOneYear}">
	    <input type="image"
	        src="https://www.sandbox.paypal.com/it_IT/i/btn/btn_buynowCC_LG.gif"
	        border="0" name="submit"
	        alt="PayPal - The safer, easier way to pay online!">
	    <img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
	    <input type="hidden" name="return" value="${returnUrl}">
	    <input type="hidden" name="custom" value="${email}">
	</form>
	
	<form action="${paypalAction}" method="post" target="_top">
		<input type="hidden" name="cmd" value="_s-xclick">
		<input type="hidden" name="hosted_button_id" value="${hostedButtonIDTwoYears}">
		<input type="image"
			src="https://www.sandbox.paypal.com/en_US/i/btn/btn_buynowCC_LG.gif"
			border="0" name="submit"
			alt="PayPal - The safer, easier way to pay online!">
		<img alt="" border="0" src="https://www.sandbox.paypal.com/en_US/i/scr/pixel.gif" width="1" height="1">
		<input type="hidden" name="return" value="${returnUrl}">
	    <input type="hidden" name="custom" value="${email}">
	</form>
	
</div>
