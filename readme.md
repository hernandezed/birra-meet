# Birra Meet

Para iniciar la aplicacion primero debemos correr docker-compose con el archivo provisto en el repositorio, el cual configurara tanto el mongo, utilizado para la persistencia, como el redis, para el cache.

Para correr los test, se necesita tener Docker instalado previamente (https://www.testcontainers.org/supported_docker_environment/)

La documentacion de la api se encuentra en: http://localhost:8080/swagger-ui.html

Customizacion del resolver:
    * El resolver permite configurar los parametros por los cuales se calculara la cantidad de cajas a comprar. Esto se puede hacer bajo la property birra-meet.provision-resolver y sus hijas.

Esquema de CI/CD
1. Jenkins Pipeline disparado por los commits al repositorio. El mismo debera correr los test del aplicativo, y fallar si la cobertura del codigo baja del 90%.
2. Uso de SonarQube para el analisis de los reportes generados por el aplicativo.
3. Jenkins Pipeline disparado por el cierre de un merge request (por ejemplo, con Gitlab, agregando la palabra deploy como comentario). El mismo tendra las siguientes etapas:
    * Check-MR: Revisa que el merge request no posea ninguna revision abierta, o conflictos de merge. En cuyo caso, falla
    * Checkout: Realiza un checkout del repositorio, parandose en el branch correspondiente
    * Test: Ejecuta los test, si alguno falla, el deploy falla
    * Code Quality: Publica los reportes en sonar. Si se viola las reglas del quality gate, falla el deploy
    * Prepare-Release: Se espera el input del usuario, el cual debe indicar la version del deploy, utilizando versionado semantico. Si la version ya existe, falla el deploy
    * deploy-st: En caso de existir un servidor para pruebas en staging, se deploya y se espera el input del usuario para continuar el pipeline
    * deploy-canary: En caso de contar con multiples instancias para deployar de manera productiva, este paso deployara solo en ciertas maquinas, para distribuir el trafico entre la nueva version y la antigua. Se espera el input del usuario para proceder
    * deploy-pr: Deploya en todas las instancias la nueva version
    * close-mr: Cierra el merge request, mergeando el branch a master
    * update-code-quality: Actualiza los reportes de SonarQube, con la nueva informacion.
    * perform-release: Publica en el repositorio de artifacts el nuevo release del aplicativo
    
Por hacer: 
1. Agregar una segunda api de clima y configurar Spring-Cloud-Circuit-Breaker, para los casos de OpenWeather no responda