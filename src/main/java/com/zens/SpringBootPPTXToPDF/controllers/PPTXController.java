package com.zens.SpringBootPPTXToPDF.controllers;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.sl.draw.DrawFactory;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;

@Controller
public class PPTXController {

    public ResponseEntity<byte[]> toPDF(MultipartFile file) {
        try {
            // Read PPT
            InputStream in = file.getInputStream();
            XMLSlideShow ppt = new XMLSlideShow(in);
            List<XSLFSlide> list = ppt.getSlides();
            in.close();

            // Get ppt page size
            Dimension pageSize = ppt.getPageSize();
            float width = pageSize.width;
            float height = pageSize.height;

            // Build PDF Document
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, out);
            pdf.setPageSize(new Rectangle(width, height));// Set page szie
            pdf.open();

            // Build PDF page
            for (XSLFSlide slide : list) {
                // Fixed Chinese font exception(Replace with available font)
                for (XSLFShape shape : slide.getShapes()) {
                    if (shape instanceof XSLFTextShape) {
                        for (XSLFTextParagraph textParagraph : (XSLFTextShape) shape) {
                            for (XSLFTextRun textRun : textParagraph) {
                                if (textRun.getFontFamily() == null) {
                                    textRun.setFontFamily("微軟正黑體");
                                }
                            }
                        }
                    }
                }

                pdf.newPage();// Build new page

                /**
                 * Conver ppt slides to png image and pdf file by pages
                 * 
                 * step
                 * - Build image buffer(awt.BufferedImage)
                 * - Use image buffer create graphics object
                 * - Setting graphics object
                 * - Draw the current slide content into a graphics object
                 * - Conver image buffer to itext image object
                 * - Append itext image object to current pdf page
                 */
                BufferedImage imageBuffer = new BufferedImage((int) width, (int) height,
                        BufferedImage.TYPE_INT_ARGB);
                Graphics2D graphics = imageBuffer.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                graphics.scale(1, 1);
                slide.draw(graphics);
                ByteArrayOutputStream imageBinary = new ByteArrayOutputStream();
                ImageIO.write(imageBuffer, "png", imageBinary);
                Image imageElement = Image.getInstance(imageBinary.toByteArray());
                imageElement.setAbsolutePosition(0, 0);
                imageElement.scaleAbsolute(width, height);
                pdf.add(imageElement);
            }
            pdf.close();

            // Output PDF file
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
            return ResponseEntity.ok().headers(headers).body(out.toByteArray());
        } catch (Exception e) {
            String message = String.format("Conver pdf is failed.[ %s ]", e.getMessage());
            return ResponseEntity.status(500).body(message.getBytes());
        }
    }

}
