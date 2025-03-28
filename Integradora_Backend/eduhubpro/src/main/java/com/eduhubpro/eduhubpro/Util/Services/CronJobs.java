package com.eduhubpro.eduhubpro.Util.Services;

import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CronJobs {

    private static final Logger logger = LoggerFactory.getLogger(CronJobs.class);

    private final UserRepository userRepository;
    private final EmailService emailService;

    @Autowired
    public CronJobs(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "10 * * * * ?")
    public void sayHello() {
        logger.info("Hello World!");
        //emailService.sendEmail("Alan", "alanyr107@gmail.com" , "Java", "Hola");
       /* List<User> users = userRepository.findAll();

        for (User user : users) {
            logger.info("Name: {}, User: {}, Role {}", user.getEmail(),user.getUserId().toString(), user.getRole());
        }*/
    }

    // todo Acciones del docente
    /* todo
     * instructor se registra
    * recupera contraseña

    * edita su perfil
             - nombre
             - correo
             - contraseña
             - calificación por parte del estudiante
    * crea y gestiona cursos
         - crear curso
          - elimina y modifica cursos
    *
    */
    // todo cursos
    /*
    se filtra por instructor, fecha,
    Atributos
            - portada
            - titulo
            - puntuación
            - fecha de inicio
            - fecha de fin
                - resumen (front)
                - sección para el contenido (front)
     * se maneja por módulos
     * dividir los módulos por fecha
    Atributos del modulo
            - nombre
            - puntuacion
            - fecha del modulo
            -  contenido (front)
            - status (bloqueado, desbloqueado)
    */
    // todo Participante
    /*
    * ver cursos
    * inscribe
    * paga
    * califica
            - certificados
            - mandar los certificados cuando termine el curso (solo los que hayan completado los modelos completos y llegue la fecha de finalizacion)
    * */
    // metricas (docente)
    /*
     * seguimiento del progreso de los estudiantes
     * */

    // todo
    /*
     * El estudiante no puede ver los cursos hasta que sea autorizado por el administrador por el
     * */
}