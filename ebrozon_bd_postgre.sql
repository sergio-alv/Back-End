
CREATE TABLE  "archivo" (
   "identificador"   int PRIMARY KEY, 
   "url"   varchar(200) NOT NULL, 
   "borrada"    smallint NOT NULL DEFAULT '0' 
)   ;



-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla archivosopinion
--

CREATE TABLE  "archivosopinion" (
   "archivo"   int NOT NULL, 
   "emisor"   varchar(30) NOT NULL, 
   "receptor"   varchar(30) NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL 
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla archivosreport
--

CREATE TABLE  "archivosreport" (
   "archivo"   int NOT NULL, 
   "emisor"   varchar(30) NOT NULL, 
   "receptor"   varchar(30) NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL 
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla archivosventa
--

CREATE TABLE  "archivosventa" (
   "archivo"   int NOT NULL, 
   "nventa" int NOT NULL,
   PRIMARY KEY("archivo","nventa")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla archivoventaverant
--

CREATE TABLE  "archivoventaverant" (
   "archivo"   int NOT NULL, 
   "nventa" int NOT NULL, 
   "fechamodventa"   timestamp without time zone NOT NULL,
   PRIMARY KEY("archivo","nventa", "fechamodventa")
)   ;

-- --------------------------------------------------------


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla etiqueta
--

CREATE TABLE  "etiqueta" (
   "nombre"   varchar(30) PRIMARY KEY, 
   "fechacreacion"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP, 
   "creador"   varchar(30) NOT NULL, 
   "nombreusuario"   varchar(30) DEFAULT NULL 
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla etiquetaventa
--

CREATE TABLE  "etiquetaventa" (
   "nventa" int NOT NULL, 
   "etiqueta"   varchar(30) NOT NULL,
   PRIMARY KEY("etiqueta","nventa")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla etiquetaventaverant
--

CREATE TABLE  "etiquetaventaverant" (
   "nventa" int NOT NULL, 
   "fechamodventa"   timestamp without time zone NOT NULL, 
   "etiqueta"   varchar(30) NOT NULL,
   PRIMARY KEY("nventa","fechamodventa","etiqueta")
)   ;



-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla mensaje
--

CREATE TABLE  "mensaje" (
   "emisor"   varchar(30) NOT NULL, 
   "receptor"   varchar(30) NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP, 
   "contenido"   text NOT NULL ,
   PRIMARY KEY("emisor","receptor","fecha")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla motivo
--

CREATE TABLE  "motivo" (
   "identificador"   int PRIMARY KEY, 
   "razonconcreta"   text NOT NULL, 
   "categoria"   varchar(50) NOT NULL 
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla oferta
--

CREATE TABLE  "oferta" (
   "usuario"   varchar(30) NOT NULL, 
   "nventa" int NOT NULL, 
   "fecha"   int NOT NULL, 
   "cantidad"   int NOT NULL DEFAULT '0', 
   "aceptada"    smallint NOT NULL DEFAULT '0',
   PRIMARY KEY("usuario","nventa","fecha")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla opinion
--

CREATE TABLE  "opinion" (
   "emisor"   varchar(30) NOT NULL, 
   "receptor"   varchar(30) NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP, 
   "contenido"   int NOT NULL, 
   "estrellas"   int NOT NULL DEFAULT '1', 
   "tienearchivo"   int NOT NULL DEFAULT '0' ,
   PRIMARY KEY("emisor","receptor","fecha")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla puja
--

CREATE TABLE  "puja" (
   "usuario"   varchar(30) NOT NULL, 
   "nventa" int NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL, 
   "cantidad"   double precision NOT NULL ,
   PRIMARY KEY("usuario","nventa","fecha")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla registrologins
--

CREATE TABLE  "registrologins" (
   "usuario"   varchar(30) NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP ,
   PRIMARY KEY("usuario","fecha")
)   ;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla report
--

CREATE TABLE  "report" (
   "emisor"   varchar(30) NOT NULL, 
   "receptor"   varchar(30) NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP, 
   "contenido"   text NOT NULL, 
   "nventa" int NOT NULL, 
   "motivo"   int NOT NULL, 
   "tienearchivo"    smallint NOT NULL DEFAULT '0' ,
   PRIMARY KEY("emisor","receptor","fecha")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla seguimiento
--

CREATE TABLE  "seguimiento" (
   "usuario"   varchar(30) NOT NULL, 
   "nventa" int NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP ,
   PRIMARY KEY("usuario","nventa")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla subasta
--

CREATE TABLE  "subasta" (
   "nventa" int PRIMARY KEY, 
   "fechafin"   timestamp without time zone NOT NULL, 
   "precioinicial"   double precision NOT NULL DEFAULT '0', 
   "pujaactual"   double precision NOT NULL DEFAULT '0'
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla usuario
--

CREATE TABLE  "usuario" (
   "nombreusuario"   varchar(30) PRIMARY KEY, 
   "correo"   varchar(100) NOT NULL, 
   "contrasena"   varchar(100) NOT NULL, 
   "telefono"   int DEFAULT NULL, 
   "nombre"   varchar(50) NOT NULL, 
   "apellidos"   varchar(100) NOT NULL, 
   "codigopostal"   int DEFAULT NULL, 
   "ciudad"   varchar(70) DEFAULT NULL, 
   "provincia"   varchar(70) DEFAULT NULL, 
   "latitud"   double precision DEFAULT NULL, 
   "longitud"   double precision DEFAULT NULL, 
   "archivo"   int NOT NULL DEFAULT '0', 
   "activo"    smallint NOT NULL DEFAULT '1' 
)   ;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla usuarioverant
--

CREATE TABLE  "usuarioverant" (
   "nombreusuario"   varchar(30) NOT NULL, 
   "fecha"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP, 
   "telefono"   int NOT NULL, 
   "nombre"   varchar(50) NOT NULL, 
   "apellidos"   varchar(100) NOT NULL, 
   "codigopostal"   int DEFAULT NULL, 
   "ciudad"   varchar(70) DEFAULT NULL, 
   "provincia"   varchar(70) DEFAULT NULL, 
   "latitud"   double precision DEFAULT NULL, 
   "longitud"   double precision DEFAULT NULL, 
   "archivo"   int DEFAULT '0', 
   "activo"    smallint NOT NULL DEFAULT '1', 
   "imagen"   int DEFAULT NULL ,
   PRIMARY KEY("nombreusuario","fecha")
)   ;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla venta
--

CREATE SEQUENCE venta_id_seq;

CREATE TABLE  "venta" (
	"identificador" int DEFAULT nextval('venta_id_seq') PRIMARY KEY,
   "usuario"   varchar(30) NOT NULL, 
   "fechainicio"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP, 
   "fechaventa"   timestamp without time zone DEFAULT NULL, 
   "producto"   varchar(100) NOT NULL, 
   "descripcion"   text NOT NULL, 
   "precio"   double precision DEFAULT '0', 
   "ciudad"   varchar(70) DEFAULT NULL,
   "preciofinal"   double precision DEFAULT NULL, 
   "comprador"   varchar(30) DEFAULT NULL, 
   "fechapago"   int DEFAULT NULL, 
   "tienearchivo"    smallint NOT NULL DEFAULT '0', 
   "activa"   int NOT NULL DEFAULT '1', 
   "essubasta"    smallint NOT NULL DEFAULT '0', 
   "es_subasta"   int NOT NULL 
)   ;


-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla ventaverant
--

CREATE TABLE  "ventaverant" (
   "nventa" int NOT NULL, 
   "fechamod"   timestamp without time zone NOT NULL DEFAULT CURRENT_TIMESTAMP, 
   "producto"   varchar(100) NOT NULL, 
   "descripcion"   text NOT NULL, 
   "precio"   double precision NOT NULL DEFAULT '0', 
   "tienevideo"    smallint NOT NULL DEFAULT '0', 
   "activa"   int NOT NULL DEFAULT '1' ,
   PRIMARY KEY("nventa","fechamod")
)   ;
