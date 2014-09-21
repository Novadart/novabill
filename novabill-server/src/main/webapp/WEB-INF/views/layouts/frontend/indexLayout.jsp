<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url value="/private/feedback" var="feedbackUrl" />
<spring:url value="/private/" var="privateUrl" />
<spring:url value="/register" var="registerPageUrl"/>
<spring:url var="pricesPageUrl" value="/prices" />

<compress:html enabled="${mvn.tiles.minify.html}" compressJavaScript="${mvn.tiles.minify.html}" compressCss="${mvn.tiles.minify.html}"> 

<!DOCTYPE html>
<!--[if IE 8]> <html lang="en" class="ie8"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9"> <![endif]-->
<!--[if !IE]><!--> <html lang="en"> <!--<![endif]-->
<!-- BEGIN HEAD -->
<head>
    <meta charset="utf-8" />
    <title>Novabill | Homepage</title>
    <meta content="width=device-width, initial-scale=1.0" name="viewport" />
    <meta content="Novabill è un servizio online per la gestione della contabilità, studiato per piccole imprese e professionisti." name="description" />
    <meta content="Novadart" name="author" />

   <!-- BEGIN GLOBAL MANDATORY STYLES -->          
   <link href="${frontendAssetsUrl}/plugins/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/plugins/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>
   <!-- END GLOBAL MANDATORY STYLES -->
   
   <!-- BEGIN PAGE LEVEL PLUGIN STYLES --> 
   <link href="${frontendAssetsUrl}/plugins/fancybox/source/jquery.fancybox.css" rel="stylesheet" />              
   <link rel="stylesheet" href="${frontendAssetsUrl}/plugins/revolution_slider/css/rs-style.css" media="screen">
   <link rel="stylesheet" href="${frontendAssetsUrl}/plugins/revolution_slider/rs-plugin/css/settings.css" media="screen"> 
   <link href="${frontendAssetsUrl}/plugins/bxslider/jquery.bxslider.css" rel="stylesheet" />                
   <!-- END PAGE LEVEL PLUGIN STYLES -->

   <!-- BEGIN THEME STYLES --> 
   <link href="${frontendAssetsUrl}/css/style-metronic.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/css/style.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/css/themes/blue.css" rel="stylesheet" type="text/css" id="style_color"/>
   <link href="${frontendAssetsUrl}/css/style-responsive.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/css/custom.css" rel="stylesheet" type="text/css"/>
   <!-- END THEME STYLES -->
   
   <link href="${frontendAssetsUrl}/plugins/cookieCuttr/cookiecuttr.css" rel="stylesheet" type="text/css"/>
   <link href="${frontendAssetsUrl}/plugins/iealert/css/style.css" rel="stylesheet" type="text/css"/>

   <link rel="shortcut icon" href="${frontendAssetsUrl}/img/favicon.png" />
	
    <style type="text/css">
    .feats-list li {
        background: url("${frontendAssetsUrl}/img/checkmark.png") no-repeat 0 50%;
        padding-left: 20px;
    
    }
	</style>
</head>
<!-- END HEAD -->

<!-- BEGIN BODY -->
<body>

    <tiles:insertAttribute name="header" />

    <!-- BEGIN PAGE CONTAINER -->  
    <div class="page-container">
        <!-- BEGIN REVOLUTION SLIDER -->    
        <div class="fullwidthbanner-container slider-main">
            <div class="fullwidthabnner">
                <ul id="revolutionul" style="display:none;">            
                        <!-- THE FIRST SLIDE -->
                        <li data-transition="fade" data-slotamount="8" data-masterspeed="700" data-delay="15000" data-thumb="${frontendAssetsUrl}/img/sliders/revolution/thumbs/thumb2.jpg">
                            <!-- THE MAIN IMAGE IN THE FIRST SLIDE -->
                            <img src="${frontendAssetsUrl}/img/sliders/revolution/bg1.jpg" alt="">
                            
                            <div class="caption lft slide_title slide_item_left"
                                 data-x="0"
                                 data-y="105"
                                 data-speed="400"
                                 data-start="1500"
                                 data-easing="easeOutExpo">
                                 FATTURAZIONE ONLINE
                            </div>
                            <div class="caption lft slide_subtitle slide_item_left"
                                 data-x="0"
                                 data-y="180"
                                 data-speed="400"
                                 data-start="2000"
                                 data-easing="easeOutExpo">
                                 Progettato per professionisti e piccole imprese
                            </div>
                            <div class="caption lft slide_desc slide_item_left"
                                 data-x="0"
                                 data-y="220"
                                 data-speed="400"
                                 data-start="2500"
                                 data-easing="easeOutExpo">
                                 <table style="margin-left: 20px;">
                                    <tr>
                                        <td>
                                            <ul class="feats-list list-unstyled">
			                                    <li>Fatture</li>
			                                    <li>Offerte</li>
			                                    <li>Documenti di Trasporto</li>
			                                    <li>Clienti</li>
			                                 </ul>                                        
                                        </td>
                                        <td>
                                             <ul style="margin-left: 40px;" class="feats-list list-unstyled">
                                                <li>Listini</li>
                                                <li>Pagamenti</li>                                                
                                                <li>Statistiche</li>
                                                <li>... molto altro!</li>
                                             </ul>
                                        </td>
                                    </tr>
                                 </table>
                            </div>
                            <div class="caption lfb"
                                 data-x="640" 
                                 data-y="55" 
                                 data-speed="700" 
                                 data-start="1000" 
                                 data-easing="easeOutExpo"  >
                                 <img src="${frontendAssetsUrl}/img/sliders/revolution/man-winner.png" alt="Image 1">
                            </div>
                        </li>
                        
                        
                        <!-- THE THIRD SLIDE -->
                        <li data-transition="fade" data-slotamount="8" data-masterspeed="700" data-delay="9400" data-thumb="assets/img/sliders/revolution/thumbs/thumb2.jpg">
                            <img src="${frontendAssetsUrl}/img/sliders/revolution/bg3.jpg" alt="">
                            <div class="caption lfl slide_item_left" 
                                 data-x="20" 
                                 data-y="95" 
                                 data-speed="400" 
                                 data-start="1500" 
                                 data-easing="easeOutBack">
                                 <a data-toggle="lightbox" data-width="853" href="https://www.youtube.com/watch?v=qWv0UQ3Q3Dk"><img src="${frontendAssetsUrl}/img/sliders/revolution/video.png" alt="Image 1"></a>
                            </div>
                            <div class="caption lfr slide_title"
                                 data-x="580"
                                 data-y="100"
                                 data-speed="400"
                                 data-start="2000"
                                 data-easing="easeOutExpo">
                                 Provare Novabill è facile
                            </div>
                            <div class="caption lfr slide_desc"
                                 data-x="580"
                                 data-y="170"
                                 data-speed="400"
                                 data-start="2500"
                                 data-easing="easeOutExpo">
                                 Guarda questo breve video che ti permette di fare un rapido tour senza doverti registrare.<br/>
                                 Puoi iniziare a usare Novabill in meno di 1 minuto.
                            </div>
                        </li> 
                        
                </ul>
                <div class="tp-bannertimer tp-bottom"></div>
            </div>
        </div>
        <!-- END REVOLUTION SLIDER -->
        
        <!-- BEGIN CONTAINER -->   
        <div class="container">
            <!-- BEGIN SERVICE BOX -->   
            <div class="row service-box">
                <div class="col-md-4 col-sm-4">
                    <div class="service-box-heading">
                        <em><i class="fa fa-location-arrow blue"></i></em>
                        <span>I tuoi documenti, ovunque</span>
                    </div>
                    <p>Porta i tuoi dati con te, consultali e modificali in ogni luogo, da qualsiasi pc. Il nostro obiettivo è assisterti nel tuo lavoro, ovunque tu sia.</p>
                </div>
                <div class="col-md-4 col-sm-4">
                    <div class="service-box-heading">
                        <em><i class="fa fa-check red"></i></em>
                        <span>Nessuna installazione</span>
                    </div>
                    <p>Novabill è un sistema cloud accessibile da tutti i browser moderni. Non devi installare nulla sul tuo pc.<br>Iniziare a utilizzare Novabill è semplice e gratuito, è richiesta solamente una rapida registrazione.</p>
                </div>
                <div class="col-md-4 col-sm-4">
                    <div class="service-box-heading">
                        <em><i class="fa fa-resize-small green"></i></em>
                        <span>Aggiornamenti costanti</span>
                    </div>
                    <p>I miglioramenti e le correzioni che apportiamo con regolarità saranno immediatamente disponibili anche per te.</p>
                </div>
            </div>
            <!-- END SERVICE BOX -->  

            <div class="clearfix"></div>
            
            <div class="row text-center">
	            <sec:authorize access="isAnonymous()">
	                <a class="btn btn-lg green col-md-4 col-md-offset-4 margin-bottom-30" href="${registerPageUrl}">Registrati</a>
	            </sec:authorize>
	            
	            <sec:authorize access="isAuthenticated()">
	                <a class="btn btn-lg green col-md-4 col-md-offset-4 margin-bottom-30" href="${privateUrl}"><i class="fa fa-file"></i> Accedi ai tuoi Documenti</a>
	            </sec:authorize>
            </div>
            
            <div class="clearfix"></div>
            
            <!-- BEGIN BLOCKQUOTE AND VIDEO -->   
            <div class="row">
                <!-- BEGIN SERVICE BLOCKS -->               
                <div class="col-md-12">
                    <h2 class="margin-bottom-30">Perché scegliere Novabill?</h2>
                    <div class="row margin-bottom-20">
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-lock color-grey"></i></div>
                            <h2>Sicurezza</h2>
                            <p>Proteggiamo i tuoi dati utilizzando lo stato dell'arte in termini di crittografia e tecnologie web. Monitoriamo e aggiorniamo regolarmente i nostri server per evitare intrusioni e attività sospette.</p>
                        </div>
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-hdd-o color-grey"></i></div>
                            <h2>Backup</h2>
                            <p>Copie di sicurezza dei tuoi dati vengono create con regolarità più volte al giorno per garantire il minimo rischio di perdita dei dati.</p>
                        </div>
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-cloud-download color-grey"></i></div>
                            <h2>I dati sono tuoi</h2>
                            <p>In ogni momento puoi scaricare una copia PDF dei tuoi documenti o un archivio ZIP che li contiene tutti. I tuoi dati rimarranno sempre ed esclusivamente di tua proprietà.</p>
                        </div>
                    </div>
                    <div class="row margin-bottom-20">
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-clock-o color-grey"></i></div>
                            <h2>Risparmia tempo</h2>
                            <p>Crea offerte, convertile in fatture e condividile immediatamente con i tuoi clienti.<br>Usa il tempo che risparmi per guadagnare di più.</p>
                        </div>
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-bar-chart-o color-grey"></i></div>
                            <h2>Tieni d'occhio l'andamento</h2>
                            <p>Valuta il tuo andamento finanziario, analizza le statistiche sulle tue vendite e tieni traccia del tuo rapporto con i clienti.</p>
                        </div>
                        <div class="col-md-4 service-box-v1">
                            <div><i class="fa fa-eur color-grey"></i></div>
                            <h2>La versione standard è gratuita</h2>
                            <p>Buona parte delle funzionalità di Novabill possono essere utilizzate gratuitamente e senza limiti di tempo. <a style="color: #000; font-style: italic;" href="${pricesPageUrl}">Ti chiederemo un contributo</a> solamente se avrai bisogno di funzionalità più avanzate.</p>
                        </div>
                    </div>
                </div>
                <!-- END SERVICE BLOCKS --> 
            </div>
            <!-- END BLOCKQUOTE AND VIDEO -->


            <div class="clearfix"></div>

        </div>
        <!-- END CONTAINER -->
    </div>
    <!-- END PAGE CONTAINER -->

    <tiles:insertAttribute name="footer" />
    
    <!-- Load javascripts at bottom, this will reduce page load time -->
    <!-- BEGIN CORE PLUGINS(REQUIRED FOR ALL PAGES) -->
    <!--[if lt IE 9]>
    <script src="${frontendAssetsUrl}/plugins/respond.min.js"></script>  
    <![endif]-->  
    <script src="${frontendAssetsUrl}/plugins/jquery-1.10.2.min.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/jquery.cookie-1.4.0.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/cookieCuttr/jquery.cookiecuttr.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
    <script src="${frontendAssetsUrl}/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>      
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/hover-dropdown.js"></script>
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/back-to-top.js"></script>    
    <!-- END CORE PLUGINS -->

    <!-- BEGIN PAGE LEVEL JAVASCRIPTS(REQUIRED ONLY FOR CURRENT PAGE) -->
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/fancybox/source/jquery.fancybox.pack.js"></script>  
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/revolution_slider/rs-plugin/js/jquery.themepunch.plugins.min.js"></script>
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/revolution_slider/rs-plugin/js/jquery.themepunch.revolution.min.js"></script> 
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/bxslider/jquery.bxslider.min.js"></script>
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/countdown/jquery.countdown.js"></script>
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/countdown/plugin/jquery.countdown-it.js"></script>
    <script type="text/javascript" src="${frontendAssetsUrl}/plugins/ekko-lightbox-3.1.4.min.js"></script>
    
    <script src="${frontendAssetsUrl}/scripts/app.js"></script>
    <script src="${frontendAssetsUrl}/scripts/index.js"></script>
    <script src="${frontendAssetsUrl}/plugins/iealert/iealert.min.js" type="text/javascript"></script>
    
    <script type="text/javascript">
	    $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
	        event.preventDefault();
	        $(this).ekkoLightbox();
	    }); 
    
        $(function() {
            App.init();    
            App.initBxSlider();
            Index.initRevolutionSlider();
            
            if(!$.cookie('ie_alert_shown_public')){
            	$("body").iealert({
                    support:"ie8",
                    title:"Il tuo browser è vecchio e insicuro e non è supportato da Novabill",
                    text:"Non è sicuro utilizzare questo browser per lavorare su dati sensibili.<br>Per favore premi sul pulsante 'Aggiorna' qui sotto e installa una versione più recente di Internet Explorer o uno dei browser alternativi suggeriti.<br><br>Grazie",
                    upgradeTitle:"Aggiorna",
                    upgradeLink:"http://browsehappy.com/",
                    overlayClose:false,
                    closeBtn: true
                });

                $.cookie('ie_alert_shown_public', 'true', { path: '/' });
            }
        });
        
        $(function(){
            var austDay = new Date();
            austDay = new Date(2014, 3, 10);
            $('#defaultCountdown').countdown({until: austDay});
            
            //start cookie cuttr            
           	$.cookieCuttr({
           		cookieAnalyticsMessage : 'Utilizziamo i cookie per raccogliere dati statistici anonimi e migliorare il servizio. Non memorizziamo dati personali.',
           		cookieAcceptButtonText: 'Ok, ho capito',
           		cookieWhatAreLinkText : '',
           		cookieNotificationLocationBottom : true,
           		cookieDeclineButton : true,
           		cookieDeclineButtonText : 'Disabilita i cookie'
           	});    
            
        });
        
        $('#alertEmailButton').click(function(){
        	var email = $('#alertEmail').val();
        	var emailTest = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        	if(! emailTest.test(email) ){
        		alert('Sembra che l\'email inserita non sia corretta.\nRiprova per favore');
        		return;
        	}
        	
        	$.ajax({
                type: 'POST',
                url: '${feedbackUrl}',
                data: {
                    subject:'Novabill3.0 - Alert me by email', 
                    name:'Anonymous User', 
                    email: email, 
                    issue:'-', 
                    message:'-'
                },
                success: function(data) {
                	alert('Email salvata. Grazie!');
                	$('.alertEmailCont').hide('slow');
                },
                error:function(e){
                	alert('C\'è stato un problema di rete.\nPer favore riprova.');
                }
            });     
        });
        
    </script>
    
    <tiles:insertAttribute name="analytics" />
    
    <!-- END PAGE LEVEL JAVASCRIPTS -->
</body>
<!-- END BODY -->
</html>

</compress:html>
