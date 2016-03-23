<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://htmlcompressor.googlecode.com/taglib/compressor" prefix="compress" %>

<spring:url value="/frontend_assets" var="frontendAssetsUrl" />
<spring:url value="/private/feedback" var="feedbackUrl" />
<spring:url value="/private/" var="privateUrl" />
<spring:url value="/register" var="registerPageUrl"/>
<spring:url value="/resources/login_check" var="loginUrl" />
<spring:url value="/forgot-password" var="forgotPasswordUrl" />
<spring:url value="/about" var="aboutUrl" />
<spring:url value="/cookies-policy" var="cookiePolicyUrl" />
<spring:url value="/tos" var="tosUrl" />
<spring:url var="logoutUrl" value="/resources/logout" />

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
        <link href="${frontendAssetsUrl}/components/jquery.cookiebar/jquery.cookiebar.css" rel="stylesheet" type="text/css"/>

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

        <style>

            div.slide1 {
                width: 100%;
                height: 100%;
                background-image: url("${frontendAssetsUrl}/img/slider/slide1.jpg");
                background-size: cover;
                background-repeat: no-repeat;
                background-position: 50% 50%;
            }

            div.slide2 {
                width: 100%;
                height: 100%;
                background-image: url("${frontendAssetsUrl}/img/slider/slide2.jpg");
                background-size: cover;
                background-repeat: no-repeat;
                background-position: 50% 50%;
            }

            div.prices {
                width: 100%;
                height: 100%;
                background-image: url("${frontendAssetsUrl}/img/prices_parallax.jpg");
                background-size: cover;
                background-repeat: no-repeat;
                background-position: 50% 50%;
            }


        </style>
    </head>
    <!-- NAVBAR
    ================================================== -->
    <body>
    <div class="navbar-wrapper">
        <div class="container">

            <nav class="navbar navbar-default navbar-static-top">
                <div class="container">
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
                            <span class="sr-only">Navigazione</span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                            <span class="icon-bar"></span>
                        </button>
                        <a class="navbar-brand" href="#"><img src="${frontendAssetsUrl}/img/logo_thin.png"></a>
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
                            <li><a href="#features">Funzionalità</a></li>
                            <li><a href="#pricing">Quanto costa?</a></li>
                            <li><a href="http://novabill.uservoice.com/">Supporto</a></li>
                        </ul>

                    </div>
                </div>
            </nav>

        </div>
    </div>


    <!-- Carousel
    ================================================== -->
    <div id="myCarousel" class="carousel slide" data-ride="carousel">
        <!-- Indicators -->
        <ol class="carousel-indicators">
            <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
            <li data-target="#myCarousel" data-slide-to="1"></li>
        </ol>
        <div class="carousel-inner" role="listbox">
            <div class="item active">
                <div class="slide1"></div>
                <div class="container">
                    <div class="carousel-caption">
                        <h1>CREA LE TUE FATTURE ONLINE</h1>
                        <p>
                            Progettato per professionisti e piccole imprese.
                        </p>
                    </div>
                </div>
            </div>
            <div class="item">
                <div class="slide2"></div>
                <div class="container">
                    <div class="carousel-caption">
                        <h1>PROVARE NOVABILL È FACILE</h1>
                        <p>Guarda questo breve video che ti permette di fare un rapido tour senza doverti registrare.<br/>
                            Puoi iniziare a usare Novabill in meno di 1 minuto.</p>
                        <p><button class="btn btn-lg btn-primary" data-toggle="modal" data-target="#novabill-video">Guarda Video</button></p>
                    </div>
                </div>
            </div>
        </div>
        <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
            <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
            <span class="sr-only">Precedente</span>
        </a>
        <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
            <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
            <span class="sr-only">Successivo</span>
        </a>
    </div><!-- /.carousel -->


    <!-- Marketing messaging and featurettes
    ================================================== -->
    <!-- Wrap the rest of the page in another container to center all the content. -->

    <div class="container marketing">

        <!-- Three columns of text below the carousel -->
        <div class="row">
            <div class="col-lg-4">
                <img class="img-circle" src="${frontendAssetsUrl}/img/focus/document.png" alt="Generic placeholder image" width="140" height="140">
                <h2>I tuoi documenti, ovunque</h2>
                <p>Porta i tuoi dati con te, consultali e modificali in ogni luogo, da qualsiasi pc. Il nostro obiettivo è assisterti nel tuo lavoro, ovunque tu sia.</p>
            </div><!-- /.col-lg-4 -->
            <div class="col-lg-4">
                <img class="img-circle" src="${frontendAssetsUrl}/img/focus/cloud_storage.png" alt="Generic placeholder image" width="140" height="140">
                <h2>Nessuna installazione</h2>
                <p>Novabill è un sistema cloud accessibile da tutti i browser moderni. Non devi installare nulla sul tuo pc.<br>Iniziare a utilizzare Novabill è semplice e gratuito, è richiesta solamente una rapida registrazione.</p>
            </div><!-- /.col-lg-4 -->
            <div class="col-lg-4">
                <img class="img-circle" src="${frontendAssetsUrl}/img/focus/updates.png" alt="Generic placeholder image" width="140" height="140">
                <h2>Aggiornamenti costanti</h2>
                <p>I miglioramenti e le correzioni che apportiamo con regolarità saranno immediatamente disponibili anche per te.</p>
            </div><!-- /.col-lg-4 -->
        </div><!-- /.row -->


        <!-- START THE FEATURETTES -->

        <hr id="features" class="featurette-divider">

        <div class="row featurette">
            <div class="col-md-7">
                <h2 class="featurette-heading">Crea, modifica e stampa i tuoi documenti</h2>
                <p class="lead">Crea rapidamente offerte, fatture, DDT e note di credito. Genera PDF e inviali automaticamente via email ai tuoi clienti. Imposta il tuo logo e scegli il modello di documento che più ti aggrada.<br>In pochi secondi puoi condividere fatture e note di credito con il commercialista</p>
            </div>
            <div class="col-md-5">
                <img class="featurette-image img-responsive center-block" src="${frontendAssetsUrl}/img/features/fatture.png" alt="Generic placeholder image">
            </div>
        </div>

        <hr class="featurette-divider">

        <div class="row featurette">
            <div class="col-md-7 col-md-push-5">
                <h2 class="featurette-heading">Gestisci clienti, articoli e listini</h2>
                <p class="lead">Organizza con facilità i dati suoi tuoi clienti, gli articoli e i listini.</p>
            </div>
            <div class="col-md-5 col-md-pull-7">
                <img class="featurette-image img-responsive center-block" src="${frontendAssetsUrl}/img/features/listini.png" alt="Generic placeholder image">
            </div>
        </div>

        <hr class="featurette-divider">

        <div class="row featurette">
            <div class="col-md-7">
                <h2 class="featurette-heading">Tieni sotto traccia i pagamenti</h2>
                <p class="lead">Consulta in ogni momento lo stato dei pagamenti.</p>
            </div>
            <div class="col-md-5">
                <img class="featurette-image img-responsive center-block" src="${frontendAssetsUrl}/img/features/pagamenti.png" alt="Generic placeholder image">
            </div>
        </div>

        <hr class="featurette-divider">

        <div class="row featurette">
            <div class="col-md-7 col-md-push-5">
                <h2 class="featurette-heading">Statistiche sul tuo lavoro</h2>
                <p class="lead">Tieni la tua attività sotto controllo con l'aiuto di statisiche e indicatori di andamento.</p>
            </div>
            <div class="col-md-5 col-md-pull-7">
                <img class="featurette-image img-responsive center-block" src="${frontendAssetsUrl}/img/features/statistiche.png" alt="Generic placeholder image">
            </div>
        </div>

        <hr class="featurette-divider">

        <!-- /END THE FEATURETTES -->


    </div><!-- /.container -->

    <!-- Plans -->
    <div class="container prices">

        <div class="prices"></div>

        <div class="row">

            <!-- item -->
            <div id="pricing" class="col-md-4 col-md-offset-4 text-center">
                <div class="panel panel-primary panel-pricing">
                    <div class="panel-heading">
                        <h1>5,00 € / Mese</h1>
                    </div>
                    <%--<div class="panel-body text-center">--%>
                        <%--<p><strong>5,00 € / Mese</strong></p>--%>
                    <%--</div>--%>
                    <ul class="list-group text-center">
                        <li class="list-group-item"><i class="fa fa-check"></i> Creazione Offerte/Fatture/DDT/Note di Credito</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Gestione Clienti</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Esportazione dei dati in PDF/ZIP</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Template dei documenti</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Gestione Pagamenti</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Backup regolare dei dati</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Statistiche dettagliate</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Invio automatico delle fatture via email con monitoraggio</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Gestione Articoli</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Gestione Listini</li>
                        <li class="list-group-item"><i class="fa fa-check"></i> Condivisione dei documenti col commercialista</li>
                    </ul>
                    <div class="panel-footer">
                        <a class="btn btn-lg btn-block btn-success" href="#">Prova gratis per 1 mese!</a>
                    </div>
                </div>
            </div>
            <!-- /item -->
        </div>
    </div>
    <!-- /Plans -->

    <div class="container">

        <!-- FOOTER -->
        <footer>
            <p class="pull-right"><a href="#">Back to top</a></p>
            <p>&copy; 2016 <a target="_blank" href="http://www.novadart.com">Novadart</a> &middot; <a href="${aboutUrl}">Chi Siamo</a> &middot; <a href="https://www.iubenda.com/privacy-policy/257554">Privacy Policy</a> &middot; <a href="${tosUrl}">Termini di Servizio</a> &middot; <a href="${cookiePolicyUrl}">Cookie Policy</a></p>
        </footer>

    </div>


    <div id="novabill-video" class="modal fade" tabindex="-1" role="dialog">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">Registrazione e primi passi in Novabill</h4>
                </div>
                <div class="modal-body">
                    <iframe width="100%" height="400" src="https://www.youtube.com/embed/qWv0UQ3Q3Dk" frameborder="0" allowfullscreen></iframe>
                </div>
            </div><!-- /.modal-content -->
        </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->


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
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script>window.jQuery || document.write('<script src="${frontendAssetsUrl}/bower_components/jquery/dist/jquery.min.js"><\/script>')</script>
    <script src="${frontendAssetsUrl}/bower_components/jquery.cookie/jquery.cookie.js"></script>
    <script src="${frontendAssetsUrl}/components/jquery.cookiebar/jquery.cookiebar.js"></script>
    <script src="${frontendAssetsUrl}/bower_components/bootstrap/dist/js/bootstrap.min.js"></script>
    <!-- IE10 viewport hack for Surface/desktop Windows 8 bug -->
    <script src="${frontendAssetsUrl}/js/ie10-viewport-bug-workaround.js"></script>

    <script>
        var nv = $('#novabill-video');

        nv.on('hidden.bs.modal', function () {
            $('#novabill-video iframe').removeAttr('src');
        });

        nv.on('show.bs.modal', function () {
            $('#novabill-video iframe').attr('src', 'https://www.youtube.com/embed/qWv0UQ3Q3Dk');
        });

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
                acceptOnScroll: true
            });

        });
    </script>

    <tiles:insertAttribute name="analytics" />

    </body>
    </html>


</compress:html>