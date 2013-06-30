Web2Mobile
==========
Aplicação Web
Tutorial para configurar ambiente:
IDE: 
    Eclipse Juno: http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/junosr2
Plugins necessários:
    m2e: http://eclipse.org/m2e/
    egit (provavelmente já está instalado no eclipse): http://www.eclipse.org/egit/
Servidor:
    Tomcat7: http://tomcat.apache.org/download-70.cgi
    
================
No eclipse instale os plugins necessários e adicione o Tomcat7 como servidor

É necessário apontar para a pasta do jdk instalado em windows/preferences installed JREs e edite o local,
aponte para a pasta do jdk ao invés da pasta do jre.

Edite as propriedades do tomcat, clicando 2x nele, e coloque pra que o eclipse use o servidor de fato (segunda opção dos
radios buttons).

Nas pasta Server criada pelo eclipse edite o xml tomcat-users.xml, adicione as seguintes linhas dentro
da tag <tomcat-users> </tomcat-users>:

<role rolename="manager-gui"/>
<role rolename="manager-script"/>
<user password="s3cret" roles="manager-gui,manager-script" username="tomcat"/>

essas linhas definem um usuário e as permissões necessárias para ele acessaar o manager do servidor.

Importando o projeto:
No eclipse selecione o menu File/Import... no popup que foi aberto selecione Git/Projects from Git.
Na próxima tela selecione URI e adicione a URI do projeto: https://github.com/danielCarlosCE/Web2Mobile.git
Não é necessário colocar usuário e senha. Clique em Next. 
Depois que finalizar o clone do projeto clique em Cancelar.
Menu File/Import.../Projeto do Maven e selecione a pasta do projeto clonada
Depois disso clique no projeto e selcione Team/Share e selecione Git.
O projeto está importado.

Na pasta .m2 que é criada pelo maven na pasta do usuário crie um arquivo settings.xml:

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                      http://maven.apache.org/xsd/settings-1.0.0.xsd">
<localRepository/>
  <interactiveMode/>
  <usePluginRegistry/>
  <offline/>
  <pluginGroups/>
  <servers>
<server>
  <id>tomcat</id>
	<username>tomcat</username>
	<password>s3cret</password>
</server>
</servers>
  <mirrors/>
  <proxies/>
  <profiles/>
  <activeProfiles/>
</settings>

Note que na tag tomcat  definimos um usuario e senha que são os menos que colocamos no tomcat-users.xml

Feito isso, inicie o Tomcat e clique no botão direito do proejto e selecione Run/(5)Maven Build... 
No popup que aparecer coloque como Goals: clean tomcat:redeploy e selecione run. 
Na setinha ao lado do botão run fica o histórico, quando mudar algo no projeto clique na setinha e selione
o mesmo Run que você criou para que o projeto seja editado no servidor. 

That's all folks!
