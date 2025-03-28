package com.eduhubpro.eduhubpro.Entity.Attendance.Service;

/*
@Transactional
@Service
public class AttendanceService {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<Message> getById(AttendanceDto dto) {
        Optional<Attendance> attendances = attendanceRepository.findById(dto.getAttendanceId().to);
        logger.info("La búsqueda de asistencias ha sido realizada correctamente {}", attendances.get().getAttendanceId());
        return new ResponseEntity<>(new Message(attendances, "Listado de asistencia", TypesResponse.SUCCESS), HttpStatus.OK);
    }

}*/