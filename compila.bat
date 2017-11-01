@echo off

if not exist bin (
	echo  *** Criando Diretorio bin ***
	mkdir bin
)

echo  *** Compilando projeto ***
javac -sourcepath src -d bin src/*.java -encoding UTF-8