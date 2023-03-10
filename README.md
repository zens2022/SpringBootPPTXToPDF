# Spring Boot pptx to pdf

## Description
This API uses Java spring boot to convert PowerPoint to PDF  

Language：`Java 11`  
Framework：`Spring Boot 2.7.7`  
Maven：`3.8.3`  
Dependencies：  
- Spring Web
- Thymeleaf
- Apache POI Common ( [LINK](https://mvnrepository.com/artifact/org.apache.poi/poi) ) ( [DOC](https://poi.apache.org/apidocs/5.0/) )
- Apache POI API Based On OPC and OOXML Schemas ( [LINK](https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml) ) ( [DOC](https://poi.apache.org/apidocs/5.0/) )
- IText Core ( [LINK](https://mvnrepository.com/artifact/com.itextpdf/itextpdf) ) ( [DOC](https://api.itextpdf.com/iText5/java/5.5.13.3/) )

## Quick Start
``` sh
mvn clean package
cd target
java -jar SpringBootPPTXToPDF-0.0.1-SNAPSHOT.jar
```

## API
| #   | Path                            | caption            |
| --- | ------------------------------- | ------------------ |
| 1   | /SpringBootPPTXToPDF/pptx/toPDF | Conver pptx to pdf |

### 1. PPTX to PDF ( [Code](./src/main/java/com/zens/SpringBootPPTXToPDF/controllers/PPTXController.java?plain=1#L34-L109) )
URL：( host ) `/SpringBootPPTXToPDF/pptx/toPDF`  
Description：Conver pptx to pdf file  
Request params：
```
FormData {
    file: (pptx file binary)
}
```
Response Data：`PDF Binary`  

PostMan Params Reference：  

![postman_params_pptx_to_pdf.PNG](./images/postman_params_pptx_to_pdf.PNG)