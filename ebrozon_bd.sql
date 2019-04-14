-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 14-04-2019 a las 17:29:28
-- Versión del servidor: 10.1.36-MariaDB
-- Versión de PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `ebrozon_bd`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivo`
--

CREATE TABLE `archivo` (
  `identificador` int(11) NOT NULL,
  `url` varchar(200) COLLATE utf8_spanish_ci NOT NULL,
  `borrada` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `archivo`
--

INSERT INTO `archivo` (`identificador`, `url`, `borrada`) VALUES
(1, 'imagen por defecto', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivosopinion`
--

CREATE TABLE `archivosopinion` (
  `archivo` int(11) NOT NULL,
  `emisor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `receptor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivosreport`
--

CREATE TABLE `archivosreport` (
  `archivo` int(11) NOT NULL,
  `emisor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `receptor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivosventa`
--

CREATE TABLE `archivosventa` (
  `archivo` int(11) NOT NULL,
  `nventa` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `archivoventaverant`
--

CREATE TABLE `archivoventaverant` (
  `archivo` int(11) NOT NULL,
  `nventa` int(11) NOT NULL,
  `fechamodventa` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `etiqueta`
--

CREATE TABLE `etiqueta` (
  `nombre` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fechacreacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `creador` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `nombreusuario` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `etiquetaventa`
--

CREATE TABLE `etiquetaventa` (
  `nventa` int(11) NOT NULL,
  `etiqueta` varchar(30) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `etiquetaventaverant`
--

CREATE TABLE `etiquetaventaverant` (
  `nventa` int(11) NOT NULL,
  `fechamodventa` datetime NOT NULL,
  `etiqueta` varchar(30) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `hibernate_sequence`
--

CREATE TABLE `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `mensaje`
--

CREATE TABLE `mensaje` (
  `emisor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `receptor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `contenido` text COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `motivo`
--

CREATE TABLE `motivo` (
  `identificador` int(11) NOT NULL,
  `razonconcreta` text COLLATE utf8_spanish_ci NOT NULL,
  `categoria` varchar(50) COLLATE utf8_spanish_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `oferta`
--

CREATE TABLE `oferta` (
  `usuario` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `nventa` int(11) NOT NULL,
  `fecha` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL DEFAULT '0',
  `aceptada` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `opinion`
--

CREATE TABLE `opinion` (
  `emisor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `receptor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `contenido` int(11) NOT NULL,
  `estrellas` int(11) NOT NULL DEFAULT '1',
  `tienearchivo` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `puja`
--

CREATE TABLE `puja` (
  `usuario` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `nventa` int(11) NOT NULL,
  `fecha` datetime NOT NULL,
  `cantidad` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `registrologins`
--

CREATE TABLE `registrologins` (
  `usuario` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `registrologins`
--

INSERT INTO `registrologins` (`usuario`, `fecha`) VALUES
('admin1', '2019-04-09 23:30:56'),
('admin1', '2019-04-09 23:45:53');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `report`
--

CREATE TABLE `report` (
  `emisor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `receptor` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `contenido` text COLLATE utf8_spanish_ci NOT NULL,
  `nventa` int(11) NOT NULL,
  `motivo` int(11) NOT NULL,
  `tienearchivo` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `seguimiento`
--

CREATE TABLE `seguimiento` (
  `usuario` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `nventa` int(11) NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `subasta`
--

CREATE TABLE `subasta` (
  `nventa` int(11) NOT NULL,
  `fechafin` datetime NOT NULL,
  `precioinicial` double NOT NULL DEFAULT '0',
  `pujaactual` double NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `nombreusuario` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `correo` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `contrasena` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `telefono` int(11) DEFAULT NULL,
  `nombre` varchar(50) COLLATE utf8_spanish_ci NOT NULL,
  `apellidos` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `codigopostal` int(11) DEFAULT NULL,
  `ciudad` varchar(70) COLLATE utf8_spanish_ci DEFAULT NULL,
  `provincia` varchar(70) COLLATE utf8_spanish_ci DEFAULT NULL,
  `latitud` double DEFAULT NULL,
  `longitud` double DEFAULT NULL,
  `archivo` int(11) NOT NULL DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`nombreusuario`, `correo`, `contrasena`, `telefono`, `nombre`, `apellidos`, `codigopostal`, `ciudad`, `provincia`, `latitud`, `longitud`, `archivo`, `activo`) VALUES
('admin', 'admin@admin.com', 'cwecasdvev', NULL, 'admin', 'admin', NULL, NULL, NULL, NULL, NULL, 1, 1),
('admin1', 'admin@admin1.com', '21232f297a57a5a743894a0e4a801fc3', NULL, 'admin', 'admin', NULL, NULL, NULL, NULL, NULL, 1, 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarioverant`
--

CREATE TABLE `usuarioverant` (
  `nombreusuario` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fecha` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `telefono` int(11) NOT NULL,
  `nombre` varchar(50) COLLATE utf8_spanish_ci NOT NULL,
  `apellidos` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `codigopostal` int(11) DEFAULT NULL,
  `ciudad` varchar(70) COLLATE utf8_spanish_ci DEFAULT NULL,
  `provincia` varchar(70) COLLATE utf8_spanish_ci DEFAULT NULL,
  `latitud` double DEFAULT NULL,
  `longitud` double DEFAULT NULL,
  `archivo` int(11) DEFAULT '0',
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `imagen` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `venta`
--

CREATE TABLE `venta` (
  `identificador` int(11) NOT NULL,
  `usuario` varchar(30) COLLATE utf8_spanish_ci NOT NULL,
  `fechainicio` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fechaventa` datetime DEFAULT NULL,
  `producto` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `descripcion` text COLLATE utf8_spanish_ci NOT NULL,
  `ciudad` varchar(70) COLLATE utf8_spanish_ci DEFAULT NULL,
  `precio` double DEFAULT '0',
  `preciofinal` double DEFAULT NULL,
  `comprador` varchar(30) COLLATE utf8_spanish_ci DEFAULT NULL,
  `fechapago` int(11) DEFAULT NULL,
  `tienearchivo` tinyint(1) NOT NULL DEFAULT '0',
  `activa` int(11) NOT NULL DEFAULT '1',
  `esSubasta` tinyint(1) NOT NULL DEFAULT '0',
  `es_subasta` int(11) NOT NULL,
  `identificadro` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ventaverant`
--

CREATE TABLE `ventaverant` (
  `nventa` int(11) NOT NULL,
  `fechamod` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `producto` varchar(100) COLLATE utf8_spanish_ci NOT NULL,
  `descripcion` text COLLATE utf8_spanish_ci NOT NULL,
  `precio` double NOT NULL DEFAULT '0',
  `tienevideo` tinyint(1) NOT NULL DEFAULT '0',
  `activa` int(11) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `archivo`
--
ALTER TABLE `archivo`
  ADD PRIMARY KEY (`identificador`);

--
-- Indices de la tabla `archivosopinion`
--
ALTER TABLE `archivosopinion`
  ADD PRIMARY KEY (`archivo`,`emisor`,`receptor`,`fecha`),
  ADD KEY `emisor` (`emisor`),
  ADD KEY `receptor` (`receptor`);

--
-- Indices de la tabla `archivosreport`
--
ALTER TABLE `archivosreport`
  ADD PRIMARY KEY (`archivo`,`emisor`,`receptor`,`fecha`),
  ADD KEY `emisor` (`emisor`),
  ADD KEY `receptor` (`receptor`);

--
-- Indices de la tabla `archivosventa`
--
ALTER TABLE `archivosventa`
  ADD PRIMARY KEY (`archivo`,`nventa`),
  ADD KEY `nventa` (`nventa`);

--
-- Indices de la tabla `archivoventaverant`
--
ALTER TABLE `archivoventaverant`
  ADD PRIMARY KEY (`archivo`,`nventa`,`fechamodventa`),
  ADD KEY `archivoventaverant_ibfk_1` (`nventa`),
  ADD KEY `archivoventaverant_ibfk_2` (`fechamodventa`);

--
-- Indices de la tabla `etiqueta`
--
ALTER TABLE `etiqueta`
  ADD PRIMARY KEY (`nombre`),
  ADD KEY `creador` (`creador`);

--
-- Indices de la tabla `etiquetaventa`
--
ALTER TABLE `etiquetaventa`
  ADD PRIMARY KEY (`nventa`,`etiqueta`),
  ADD KEY `nventa` (`nventa`),
  ADD KEY `etiquetaventa_ibfk_3` (`etiqueta`);

--
-- Indices de la tabla `etiquetaventaverant`
--
ALTER TABLE `etiquetaventaverant`
  ADD PRIMARY KEY (`nventa`,`fechamodventa`,`etiqueta`),
  ADD KEY `etiquetaventaverant_ibfk_1` (`nventa`),
  ADD KEY `etiquetaventaverant_ibfk_3` (`fechamodventa`),
  ADD KEY `etiquetaventaverant_ibfk_4` (`etiqueta`);

--
-- Indices de la tabla `mensaje`
--
ALTER TABLE `mensaje`
  ADD PRIMARY KEY (`emisor`,`receptor`,`fecha`),
  ADD KEY `mensaje_ibfk_2` (`receptor`);

--
-- Indices de la tabla `motivo`
--
ALTER TABLE `motivo`
  ADD PRIMARY KEY (`identificador`);

--
-- Indices de la tabla `oferta`
--
ALTER TABLE `oferta`
  ADD PRIMARY KEY (`usuario`,`nventa`),
  ADD KEY `nventa` (`nventa`);

--
-- Indices de la tabla `opinion`
--
ALTER TABLE `opinion`
  ADD PRIMARY KEY (`emisor`,`receptor`,`fecha`),
  ADD KEY `receptor` (`receptor`);

--
-- Indices de la tabla `puja`
--
ALTER TABLE `puja`
  ADD PRIMARY KEY (`usuario`,`nventa`,`fecha`),
  ADD KEY `nventa` (`nventa`);

--
-- Indices de la tabla `registrologins`
--
ALTER TABLE `registrologins`
  ADD PRIMARY KEY (`usuario`,`fecha`);

--
-- Indices de la tabla `report`
--
ALTER TABLE `report`
  ADD PRIMARY KEY (`emisor`,`receptor`,`fecha`),
  ADD KEY `report_ibfk_2` (`receptor`),
  ADD KEY `motivo` (`motivo`),
  ADD KEY `nventa` (`nventa`);

--
-- Indices de la tabla `seguimiento`
--
ALTER TABLE `seguimiento`
  ADD PRIMARY KEY (`usuario`,`nventa`),
  ADD KEY `seguimiento_ibfk_1` (`nventa`),
  ADD KEY `seguimiento_ibfk_3` (`usuario`);

--
-- Indices de la tabla `subasta`
--
ALTER TABLE `subasta`
  ADD PRIMARY KEY (`nventa`),
  ADD KEY `nventa` (`nventa`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`nombreusuario`),
  ADD UNIQUE KEY `Correos` (`correo`),
  ADD UNIQUE KEY `Telefonos` (`telefono`),
  ADD KEY `usuario_ibfk_1` (`archivo`);

--
-- Indices de la tabla `usuarioverant`
--
ALTER TABLE `usuarioverant`
  ADD PRIMARY KEY (`nombreusuario`,`fecha`);

--
-- Indices de la tabla `venta`
--
ALTER TABLE `venta`
  ADD PRIMARY KEY (`identificador`),
  ADD KEY `venta_ibfk_1` (`usuario`),
  ADD KEY `venta_ibfk_2` (`comprador`);

--
-- Indices de la tabla `ventaverant`
--
ALTER TABLE `ventaverant`
  ADD PRIMARY KEY (`nventa`,`fechamod`),
  ADD KEY `ventaverant_ibfk_1` (`nventa`),
  ADD KEY `ventaverant_ibfk_2` (`fechamod`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `archivo`
--
ALTER TABLE `archivo`
  MODIFY `identificador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT de la tabla `motivo`
--
ALTER TABLE `motivo`
  MODIFY `identificador` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `venta`
--
ALTER TABLE `venta`
  MODIFY `identificador` int(11) NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `archivosopinion`
--
ALTER TABLE `archivosopinion`
  ADD CONSTRAINT `archivosopinion_ibfk_1` FOREIGN KEY (`archivo`) REFERENCES `archivo` (`identificador`),
  ADD CONSTRAINT `archivosopinion_ibfk_2` FOREIGN KEY (`emisor`) REFERENCES `opinion` (`emisor`),
  ADD CONSTRAINT `archivosopinion_ibfk_3` FOREIGN KEY (`receptor`) REFERENCES `opinion` (`receptor`);

--
-- Filtros para la tabla `archivosreport`
--
ALTER TABLE `archivosreport`
  ADD CONSTRAINT `archivosreport_ibfk_1` FOREIGN KEY (`archivo`) REFERENCES `archivo` (`identificador`),
  ADD CONSTRAINT `archivosreport_ibfk_2` FOREIGN KEY (`emisor`) REFERENCES `report` (`emisor`),
  ADD CONSTRAINT `archivosreport_ibfk_3` FOREIGN KEY (`receptor`) REFERENCES `report` (`receptor`);

--
-- Filtros para la tabla `archivosventa`
--
ALTER TABLE `archivosventa`
  ADD CONSTRAINT `archivosventa_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `venta` (`identificador`),
  ADD CONSTRAINT `archivosventa_ibfk_3` FOREIGN KEY (`archivo`) REFERENCES `archivo` (`identificador`);

--
-- Filtros para la tabla `archivoventaverant`
--
ALTER TABLE `archivoventaverant`
  ADD CONSTRAINT `archivoventaverant_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `ventaverant` (`nventa`),
  ADD CONSTRAINT `archivoventaverant_ibfk_2` FOREIGN KEY (`fechamodventa`) REFERENCES `ventaverant` (`fechamod`),
  ADD CONSTRAINT `archivoventaverant_ibfk_3` FOREIGN KEY (`archivo`) REFERENCES `archivo` (`identificador`);

--
-- Filtros para la tabla `etiqueta`
--
ALTER TABLE `etiqueta`
  ADD CONSTRAINT `etiqueta_ibfk_1` FOREIGN KEY (`creador`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `etiquetaventa`
--
ALTER TABLE `etiquetaventa`
  ADD CONSTRAINT `etiquetaventa_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `venta` (`identificador`),
  ADD CONSTRAINT `etiquetaventa_ibfk_3` FOREIGN KEY (`etiqueta`) REFERENCES `etiqueta` (`nombre`);

--
-- Filtros para la tabla `etiquetaventaverant`
--
ALTER TABLE `etiquetaventaverant`
  ADD CONSTRAINT `etiquetaventaverant_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `ventaverant` (`nventa`),
  ADD CONSTRAINT `etiquetaventaverant_ibfk_2` FOREIGN KEY (`fechamodventa`) REFERENCES `ventaverant` (`fechamod`),
  ADD CONSTRAINT `etiquetaventaverant_ibfk_4` FOREIGN KEY (`etiqueta`) REFERENCES `etiqueta` (`nombre`);

--
-- Filtros para la tabla `mensaje`
--
ALTER TABLE `mensaje`
  ADD CONSTRAINT `mensaje_ibfk_1` FOREIGN KEY (`emisor`) REFERENCES `usuario` (`nombreusuario`),
  ADD CONSTRAINT `mensaje_ibfk_2` FOREIGN KEY (`receptor`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `oferta`
--
ALTER TABLE `oferta`
  ADD CONSTRAINT `oferta_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `venta` (`identificador`),
  ADD CONSTRAINT `oferta_ibfk_3` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `opinion`
--
ALTER TABLE `opinion`
  ADD CONSTRAINT `opinion_ibfk_1` FOREIGN KEY (`emisor`) REFERENCES `usuario` (`nombreusuario`),
  ADD CONSTRAINT `opinion_ibfk_2` FOREIGN KEY (`receptor`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `puja`
--
ALTER TABLE `puja`
  ADD CONSTRAINT `puja_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `subasta` (`nventa`),
  ADD CONSTRAINT `puja_ibfk_2` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `registrologins`
--
ALTER TABLE `registrologins`
  ADD CONSTRAINT `registrologins_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `report`
--
ALTER TABLE `report`
  ADD CONSTRAINT `report_ibfk_1` FOREIGN KEY (`emisor`) REFERENCES `usuario` (`nombreusuario`),
  ADD CONSTRAINT `report_ibfk_2` FOREIGN KEY (`receptor`) REFERENCES `usuario` (`nombreusuario`),
  ADD CONSTRAINT `report_ibfk_3` FOREIGN KEY (`motivo`) REFERENCES `motivo` (`identificador`),
  ADD CONSTRAINT `report_ibfk_4` FOREIGN KEY (`nventa`) REFERENCES `venta` (`identificador`);

--
-- Filtros para la tabla `seguimiento`
--
ALTER TABLE `seguimiento`
  ADD CONSTRAINT `seguimiento_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `venta` (`identificador`),
  ADD CONSTRAINT `seguimiento_ibfk_3` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `subasta`
--
ALTER TABLE `subasta`
  ADD CONSTRAINT `subasta_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `venta` (`identificador`);

--
-- Filtros para la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `usaurio_ibfk_1` FOREIGN KEY (`archivo`) REFERENCES `archivo` (`identificador`);

--
-- Filtros para la tabla `usuarioverant`
--
ALTER TABLE `usuarioverant`
  ADD CONSTRAINT `usuarioverant_ibfk_1` FOREIGN KEY (`nombreusuario`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `venta`
--
ALTER TABLE `venta`
  ADD CONSTRAINT `venta_ibfk_1` FOREIGN KEY (`usuario`) REFERENCES `usuario` (`nombreusuario`),
  ADD CONSTRAINT `venta_ibfk_2` FOREIGN KEY (`comprador`) REFERENCES `usuario` (`nombreusuario`);

--
-- Filtros para la tabla `ventaverant`
--
ALTER TABLE `ventaverant`
  ADD CONSTRAINT `ventaverant_ibfk_1` FOREIGN KEY (`nventa`) REFERENCES `venta` (`identificador`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
