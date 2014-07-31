<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:url var="frontendAssetsUrl" value="/frontend_assets" />
<spring:url var="homeUrl" value="/" />

<div class="page-container">
  
        <!-- BEGIN BREADCRUMBS -->   
        <div class="row breadcrumbs margin-bottom-40">
            <div class="container">
                <div class="col-md-4 col-sm-4">
                    <h1>Funzionalità</h1>
                </div>
                <div class="col-md-8 col-sm-8">
                    <ul class="pull-right breadcrumb">
                        <li><a href="${homeUrl}">Home</a></li>
                        <li class="active">Funzionalità</li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- END BREADCRUMBS -->

        
        <!-- BEGIN CONTAINER -->   
      <div class="container min-hight portfolio-page margin-bottom-30">
         
         <div class="row">
             <div class="col-md-5">
                 <img class="img-responsive" src="${frontendAssetsUrl}/img/features/fatture.png">
             </div>

             <div class="col-md-7 text-left">
                 <h2>Crea, modifica e stampa i tuoi documenti</h2>
                 <p>Crea rapidamente offerte, fatture, DDT e note di credito. Genera PDF e inviali automaticamente via email ai tuoi clienti.</p>
                 <p>Imposta il tuo logo e scegli il modello di documento che più ti aggrada.</p>
                 <p>In pochi secondi puoi condividere fatture e note di credito con il commercialista</p>
             </div>
             
         </div>
         
         <br>
         <br>
         <br>
         
         <div class="row">
             
             <div class="col-md-7 text-right">
                 <h2>Gestisci clienti, articoli e listini</h2>
                 <p>Organizza con facilità i dati suoi tuoi clienti, gli articoli e i listini.</p>
             </div>
             
             <div class="col-md-5">
                 <img class="img-responsive" src="${frontendAssetsUrl}/img/features/listini.png">
             </div>

         </div>
         
         <br>
         <br>
         <br>
         
         <div class="row">
             <div class="col-md-5">
                 <img class="img-responsive" src="${frontendAssetsUrl}/img/features/pagamenti.png">
             </div>

             <div class="col-md-7">
                 <h2>Tieni sotto traccia i pagamenti</h2>
                 <p>Consulta in ogni momento lo stato dei pagamenti.</p>
             </div>
             
         </div>
         
         <br>
         <br>
         <br>
         
         <div class="row">
             
             <div class="col-md-7 text-right">
                 <h2>Statistiche sul tuo lavoro</h2>
                 <p>Tieni la tua attività sotto controllo con l'aiuto di statisiche e indicatori di andamento.</p>
             </div>
             
             <div class="col-md-5">
                 <img class="img-responsive" src="${frontendAssetsUrl}/img/features/statistiche.png">
             </div>

         </div>
         
      </div>
        
</div>