@echo off

if exist bin (
	echo  *** Iniciando RMI Registry ***
	cd bin
	start rmiregistry
) else (
	echo  *** Diretorio bin nao existe ***
)
cd..