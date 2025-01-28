CREATE TABLE IF NOT EXISTS `curriculo` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `telefone` varchar(100) NOT NULL,
  `cargo_desejado` varchar(100) NOT NULL,
  `escolaridade` varchar(100) NOT NULL,
  `observacoes` varchar(100) DEFAULT NULL,
  `ip` varchar(100) NOT NULL,
  `data_envio` date NOT NULL,
  PRIMARY KEY (`co_curriculo`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2449;

CREATE TABLE IF NOT EXISTS `arquivo` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `nome_aquivo` blob NOT NULL,
  `arquivo` varchar(100) NOT NULL,
  `content_type` varchar(100) NOT NULL,
  `size` varchar(100) NOT NULL,
  PRIMARY KEY (`co_curriculo`)
) ENGINE=MyISAM  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2449;