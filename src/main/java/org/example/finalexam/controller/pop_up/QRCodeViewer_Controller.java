package org.example.finalexam.controller.pop_up;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.finalexam.utils.FXMLSupport;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;


public class QRCodeViewer_Controller {

    @FXML
    private ImageView iv_QRCode;

    @FXML
    public void generateQRCode() {
        String qrCodeText = "https://mvix.com/blog/top-5-locations-to-place-movie-theater-signage/";
        String uniqueQrCodeText = qrCodeText + "?timestamp=" + Instant.now().toEpochMilli();
        int width = 300;
        int height = 300;

        try {
            BufferedImage qrCodeImage = generateQRCodeImage(uniqueQrCodeText, width, height);
            Image fxImage = convertToFXImage(qrCodeImage);
            iv_QRCode.setImage(fxImage);
        } catch (WriterException | IOException e) {
            FXMLSupport.showAlert(Alert.AlertType.ERROR, "QR code generation failed", "Error while generating QR code");
        }
    }

    private BufferedImage generateQRCodeImage(String text, int width, int height)
            throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        return MatrixToImageWriter.toBufferedImage(bitMatrix);
    }

    private Image convertToFXImage(BufferedImage bufferedImage) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", os);
        ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
        return new Image(is);
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) iv_QRCode.getScene().getWindow();
        stage.close();
    }
}
