<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="registerUrl" value="/register" />
<spring:url var="tosUrl" value="/tos-minimal" />

<link href="${frontendAssetsUrl}/bower_components/ekko-lightbox/dist/ekko-lightbox.min.css" rel="stylesheet">
<script src="${frontendAssetsUrl}/bower_components/ekko-lightbox/dist/ekko-lightbox.min.js"></script>

<script src="${frontendAssetsUrl}/bower_components/zxcvbn/dist/zxcvbn.js"></script>
<script src="${frontendAssetsUrl}/bower_components/pwstrength-bootstrap/dist/pwstrength-bootstrap-1.2.10.min.js"></script>

<style>
    
    .progress {
        margin-bottom: 0px;
    }
    
</style>

<div class="container">


    <div class="row">

        <div class="col-md-8 col-md-offset-2 text-center">
            <h3>Inserisci i tuoi dati <small style="margin-left: 10px;">(Dubbi? guarda il <a data-toggle="lightbox" data-width="853" href="https://www.youtube.com/watch?v=qWv0UQ3Q3Dk">video di esempio</a>)</small>
            </h3>
            <br><br>
        </div>



        <div class="col-md-8 col-md-offset-2">

            <form:form action="${registerUrl}" modelAttribute="registration" cssClass="form-horizontal" method="post" class="form-horizontal">

                <div class="form-group">
                    <label for="email" class="col-sm-4 control-label">Email</label>
                    <div class="col-sm-8">
                        <form:input path="email" cssClass="form-control" type="email" placeholder="La tua e-mail" />
                        <span class="text-danger"><form:errors path="email" /></span>
                    </div>
                </div>

                <div class="form-group">
                    <label for="register_password" class="col-sm-4 control-label">Password</label>
                    <div class="col-sm-8">
                        <form:password path="password" cssClass="form-control password placeholder-no-fix password-strength" autocomplete="off"
                                       id="register_password" placeholder="Crea una Password"
                                       data-toggle="popover" data-placement="left" data-trigger="focus"
                                       data-content="La password deve essere lunga almeno 8 caratteri.<br><br><small style='font-style:italic;'><b>Consiglio</b> - per una password sicura includi:<ul><li>caratteri sia maiuscoli che minuscoli</li><li>almeno un numero</li><li>almeno un carattere speciale come : , / % $ ! . # @ *</li></ul></small>"/>
                        <span class="text-danger"><form:errors path="password" /> </span>
                    </div>
                </div>
                <script type="text/javascript">
                    $(function(){
                        $('input.password').popover({
                            html : true
                        });


                        var initialized = false;
                        var input = $("#register_password");


                        input.pwstrength({
                            common: {
                                minChar: 8,
                                zxcvbn: true
                            },

                            ui: {
                                showVerdictsInsideProgressBar: true,
                                verdicts: ["", "Ok ma debole", "Discretamente Sicura", "Abbastanza Sicura", "Sicura"],
                                scores: [17, 26, 40, 50, 60]
                            }

                        });



                    });
                </script>


                <div class="form-group">
                    <label for="confirmPassword" class="col-sm-4 control-label">Conferma Password</label>
                    <div class="col-sm-8">
                        <form:password path="confirmPassword" cssClass="form-control placeholder-no-fix" autocomplete="off"
                                       id="register_password" placeholder="Conferma Password" />
                        <span class="text-danger"><form:errors path="confirmPassword" /></span>
                    </div>
                </div>
                <div class="form-group">
                    <label class="col-sm-offset-4 control-label">
                        <form:checkbox path="agreementAccepted"/>
                        Accetto i <a data-toggle="modal" data-target="#tosModal" href="javascript:void(0);">
                        Termini di Servizio</a> e
                        <span style="position: relative; top: 6px;">
                            <a href="https://www.iubenda.com/privacy-policy/257554" class="iubenda-light iubenda-embed"
                               title="Privacy Policy">Privacy Policy</a>
                            </span>
                    </label>
                    <div class="col-sm-offset-4 text-danger"><form:errors path="agreementAccepted" /></div>
                </div>
                <div class="form-group">
                    <div class="col-md-12 text-center">
                        <form:button class="btn btn-success">
                            Invia
                        </form:button>
                    </div>
                </div>
            </form:form>

        </div>
    </div>

</div>

<!-- Modal -->
<div class="modal fade" id="tosModal" tabindex="-1" role="dialog" aria-labelledby="tos" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-hidden="true">&times;</button>
                <h4 class="modal-title">Termini di Servizio</h4>
            </div>
            <div class="modal-body">
                <iframe style="width: 100%; height: 100%; border: 1px solid #DDD;" src="${tosUrl}"></iframe>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">
    $(function(){
        $(document).delegate('*[data-toggle="lightbox"]', 'click', function(event) {
            event.preventDefault();
            $(this).ekkoLightbox();
        });


    });

    $('#tosModal').on('show.bs.modal', function () {
        alert('asdsa');
        $('#tosModal .modal-body iframe').css('height', $(window).height()*0.8);
    });
</script>

