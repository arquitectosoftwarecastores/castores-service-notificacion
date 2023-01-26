package com.grupocastores.notificacion.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;




@Repository
public class NotificacionRepository{
    
    
    @PersistenceContext
    private EntityManager entityManager;
    
    static final String queryFindTalones =
            "SELECT * FROM OPENQUERY(%s, ' SELECT * FROM ( SELECT tr.cla_talon AS clatalon, tr.nomorigen, tr.calleorigen, tr.nomdestino, tr.calledestino, et.idesquema, et.idnegociacion, et.idcliente, et.idoficina, tr.importeseguro,tr.recoleccion, tr.entrega, tr.maniobras, tr.ferry, tr.revac, tr.otroscargos, tr.gps, tr.importesubtotal, tr.importeiva, tr.importeiva_ret AS importeivaret,tr.otras_lineas AS otraslineas, tr.importetotal, tr.val_decl AS valdecl, tr.seentregara, tr.remision FROM talones.tr%s tr  INNER JOIN talones.especificacion_talon et ON tr.cla_talon = et.cla_talon INNER JOIN talones.ajustesgenerales taj ON  (tr.idofirte = taj.idlugarorigen OR tr.idofirte = taj.oficinaajusta) AND (tr.idofidest = taj.idlugardestino OR tr.idofidest = taj.oficinaajusta) WHERE tr.idclasificaciondoc = 2 AND tr.no_guia IS NULL AND tr.tipounidad = %s AND et.idesquema = %s AND et.idcliente = %s AND et.idoficina = \"%s\" AND tr.idcdrec IN (%s) AND tr.idcddes IN (%s)  ORDER BY taj.porcentaje DESC ) AS tem GROUP BY tem.clatalon');";
    
    static final String queryGetEspecificacionTalon =
            "SELECT * FROM OPENQUERY(%s, 'SELECT tr.cla_talon, tet.idesquema, tr.tipounidad, tr.idcddes, tr.idcdrec FROM talones.tr%s tr INNER JOIN talones.especificacion_talon tet ON tr.cla_talon = tet.cla_talon WHERE tr.tp_dc = 1 AND tr.no_guia IS NULL  AND tr.cla_talon = \"%s\";');";
    
    static final String queryGetTablaTalon =
            "SELECT * FROM OPENQUERY(%s, 'SELECT * FROM talones.talones t WHERE t.cla_talon = \"%s\";');";
    

}
