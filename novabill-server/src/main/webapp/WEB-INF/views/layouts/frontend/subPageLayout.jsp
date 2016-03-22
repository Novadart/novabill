<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url var="homeUrl" value="/" />
<spring:url var="logoutUrl" value="/resources/logout" />
<spring:url value="/resources/login_check" var="loginUrl" />
<spring:url value="/forgot-password" var="forgotPasswordUrl" />
<spring:url value="/register" var="registerPageUrl"/>

<compress:html enabled="${mvn.tiles.minify.html}" compressJavaScript="${mvn.tiles.minify.html}" compressCss="${mvn.tiles.minify.html}">

    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <meta content="Novabill è un servizio online per la gestione della contabilità, studiato per piccole imprese e professionisti." name="description" />
        <meta content="Novadart" name="author" />

        <link rel="icon" href="${frontendAssetsUrl}/img/favicon.png">

        <title>Novabill</title>

        <!-- jQuery Cookiebar -->
        <link href="${frontendAssetsUrl}/bower_components/jquery.cookiebar/dist/jquery.cookiebar.min.css" rel="stylesheet" type="text/css"/>

        <!-- Font Awesome -->
        <link href="${frontendAssetsUrl}/bower_components/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css"/>

        <!-- Bootstrap core CSS -->
        <link href="${frontendAssetsUrl}/bower_components/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet" type="text/css"/>

        <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
        <link href="${frontendAssetsUrl}/css/ie10-viewport-bug-workaround.css" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
        <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->

        <!-- Custom styles for this template -->
        <link href="${frontendAssetsUrl}/css/style.css" rel="stylesheet">

        <!-- Loading jQuery here because it might be needed in the body of the page -->
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="${frontendAssetsUrl}/bower_components/jquery/dist/jquery.min.js"><\/script>')</script>

    </head>
    <!-- NAVBAR
    ================================================== -->
    <body>
    <nav class="navbar navbar-default">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Navigazione</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="${homeUrl}"><img src="${frontendAssetsUrl}/img/logo_thin.png"></a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">

                <sec:authorize access="isAnonymous()">
                    <a type="button" data-toggle="modal" data-target="#novabill-login" class="btn btn-success navbar-btn navbar-right" style="margin-right: 5px;">Accedi / Registrati</a>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">

                    <form action="${logoutUrl}" method="post" style="margin-right: 10px;">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                        <input class="btn btn-default navbar-btn navbar-right" type="submit" value="Esci">
                    </form>

                    <a href="${privateUrl}" style="margin-right: 10px;" type="button" class="btn btn-primary navbar-btn navbar-right">Accedi ai tuoi Documenti</a>

                </sec:authorize>

                <ul class="nav navbar-nav navbar-right">
                    <li><a href="${homeUrl}#features">Funzionalità</a></li>
                    <li><a href="${homeUrl}#pricing">Quanto costa?</a></li>
                    <li><a href="http://novabill.uservoice.com/">Supporto</a></li>
                </ul>

            </div>
        </div>
    </nav>

    <tiles:insertAttribute name="body" />


    <div class="container">

        <!-- FOOTER -->
        <footer>
            <p class="pull-right"><a href="#">Back to top</a></p>
            <p>&copy; 2016 <a target="_blank" href="http://www.novadart.com">Novadart</a> &middot; <a href="#">Privacy Policy</a> &middot; <a href="#">Termini di Servizio</a> &middot; <a href="#">Cookie Policy</a></p>
        </footer>

    </div>


    <div id="novabill-login" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">Accedi</h4>
                </div>
                <div class="modal-body">

                    <form class="form-horizontal" action="${loginUrl}"  method="post">
                        <div class="form-group">
                            <label for="inputEmail3" class="col-sm-2 control-label">E-mail</label>
                            <div class="col-sm-10">
                                <input name="j_username" type="email" class="form-control" id="inputEmail3" placeholder="Email">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="inputPassword3" class="col-sm-2 control-label">Password</label>
                            <div class="col-sm-10">
                                <input name="j_password" type="password" class="form-control" id="inputPassword3" placeholder="Password">
                                <a href="${forgotPasswordUrl}">Ho dimenticato la password</a>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox" name="_spring_security_remember_me"> Ricordami per 7 giorni
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-12 text-center">
                                <button type="submit" class="btn btn-success">Accedi</button>
                                <br>
                                <a href="${registerPageUrl}">Non hai un account? Registrati</a>
                            </div>
                        </div>

                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </form>

                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->


    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="${frontendAssetsUrl}/bower_components/jquery.cookie/jquery.cookie.js"></script>
    <script src="${frontendAssetsUrl}/bower_components/jquery.cookiebar/dist/jquery.cookiebar.min.js"></script>
    <script src="${frontendAssetsUrl}/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${frontendAssetsUrl}/js/ie10-viewport-bug-workaround.js"></script>

    <script>
        $("nav ul li a[href^='#']").on('click', function(e) {

            // prevent default anchor click behavior
            e.preventDefault();

            // store hash
            var hash = this.hash;

            // animate
            $('html, body').animate({
                scrollTop: $(hash).offset().top
            }, 500, function(){

                // when done, add hash to url
                // (default click behaviour)
                window.location.hash = hash;
            });

        });


        $(function(){

            // setup the cookie bar
            var message = 'Questo sito o gli strumenti terzi da questo utilizzati si avvalgono di cookie necessari al funzionamento ed utili alle finalità illustrate nella cookie policy. ' +
                    'Se vuoi saperne di più o negare il consenso a tutti o ad alcuni cookie, consulta la cookie policy. <br>Chiudendo questo banner, scorrendo questa pagina, ' +
                    'cliccando su un link o proseguendo la navigazione in altra maniera, acconsenti all’uso dei cookie.';
            $.cookieBar({
                autoEnable: false,
                message: message,
                acceptButton: true,
                acceptText: 'Chiudi',
                policyButton: true,
                policyText: 'Cookie Policy',
                policyURL: '${cookiesPolicy}',
                fixed:true,
                zindex:'9999999',
                acceptOnContinue: true,
                acceptOnScroll: false
            });

        });
    </script>

    <script type="text/javascript">
        (function(w, d) {
            var loader = function() {
                var s = d.createElement("script"), tag = d
                        .getElementsByTagName("script")[0];
                s.src = "https://cdn.iubenda.com/iubenda.js";
                tag.parentNode.insertBefore(s, tag);
            };
            if (w.addEventListener) {
                w.addEventListener("load", loader, false);
            } else if (w.attachEvent) {
                w.attachEvent("onload", loader);
            } else {
                w.onload = loader;
            }
        })(window, document);
    </script>


    <tiles:insertAttribute name="analytics" />

    </body>
    </html>

</compress:html>