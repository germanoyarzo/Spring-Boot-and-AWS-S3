package com.example.demospringboots3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.example.demospringboots3.model.vm.Asset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class S3Service {
    private final static String BUCKET = "demospringboots3bucket";

    @Autowired
    public AmazonS3 s3Client;

    //envia un objeto a nuestro bucket
    public String putObject(MultipartFile multipartFile){
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());
        String key = String.format("%s.%s", UUID.randomUUID(), extension);

        ObjectMetadata objectMetadata= new ObjectMetadata();
        objectMetadata.setContentType(multipartFile.getContentType());

        try{
            PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET, key, multipartFile.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead); ///para que sea publico la lectura
            s3Client.putObject(putObjectRequest);
            return key;
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }
    ///se obtiene mediante la llave: key
    public Asset getObject(String key){
        S3Object s3Object = s3Client.getObject(BUCKET, key);
        ObjectMetadata metadata = s3Object.getObjectMetadata();

        try{
            S3ObjectInputStream inputStream = s3Object.getObjectContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);

            return new Asset(bytes, metadata.getContentType());
        }catch (IOException ex){
            throw new RuntimeException(ex);
        }
    }


    public void deleteObjetct(String key){
        s3Client.deleteObject(BUCKET, key);
    }

    ///construir una url para los objetos publicos,esto es si el cliente quiere acceder desde una url
    public String getObjectUrl(String key){

        return String.format("https://%s.s3.amazonaws.com/s%", BUCKET, key);
    }
}
