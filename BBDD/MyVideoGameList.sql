CREATE DATABASE MyVideoGameList
GO
USE MyVideoGameList
GO

CREATE TABLE Usuarios(
	Id int IDENTITY(1,1) Not NULL CONSTRAINT PK_Usuarios PRIMARY KEY
	,Nickname varChar(15) Not NULL
	,UserPassword varChar(20) Not NULL
	,VideojuegosJugados int Not NULL
	,VideojuegosPlaneados int Not NULL
	,VideojuegosDropeados int Not NULL
	,VideojuegosEnPausa int Not NULL
	,VideojuegosJugando int Not NULL
	,esListaPrivada bit Not NULL DEFAULT 0
)
GO
CREATE TABLE Videojuegos(
	Id int IDENTITY(1,1) Not NULL CONSTRAINT PK_Videojuegos PRIMARY KEY
	,Nombre varChar(70) Not NULL
	,Desarrollador varChar(50) Not NULL
	,Distribuidores varChar(50) Not NULL
	,Plataformas varChar(60) Not NULL
	,NotaMedia float NULL
	,DificultadMedia float NULL
	,FechaDeLanzamiento DateTime Not NULL
	,Generos varChar(50) Not NULL
	,urlImagen varChar(200) Not NULL
)
GO
CREATE TABLE EstadosVideojuego(
	Id int Not NULL CONSTRAINT PK_EstadosVideojuego PRIMARY KEY
	,NombreEstado varChar(20)
)
GO
CREATE TABLE ListaVideojuegos(
	IdUsuario int Not NULL CONSTRAINT FK_Usuarios FOREIGN KEY REFERENCES Usuarios(Id)
	,IdVideojuego int Not NULL CONSTRAINT FK_Videojuegos FOREIGN KEY REFERENCES Videojuegos(Id)
	,FechaDeComienzo DateTime NULL
	,FechaDeFinalizacion DateTime NULL
	,Nota float NULL
	,Dificultad float NULL
	,Estado int Not NULL CONSTRAINT FK_EstadosVideojuego FOREIGN KEY REFERENCES EstadosVideojuego(Id)
	,CONSTRAINT PK_ListaVideojuegos PRIMARY KEY (IdUsuario,IdVideojuego)
)
GO
CREATE OR ALTER TRIGGER Calcular_NotaMedia ON ListaVideojuegos AFTER INSERT, UPDATE AS
--TRIGGER PARA CALCULAR LA NOTA MEDIA CADA VEZ QUE SE INSERTE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @NuevoIdVideojuego INT

SELECT @NuevoIdVideojuego = IdVideojuego FROM inserted

SELECT @Media = AVG(Nota) FROM ListaVideojuegos
	WHERE IdVideojuego = @NuevoIdVideojuego

	UPDATE Videojuegos
		SET NotaMedia = @Media FROM ListaVideojuegos AS LV
		WHERE Id = @NuevoIdVideojuego
GO
CREATE OR ALTER TRIGGER Calcular_NotaMediaBorrado ON ListaVideojuegos AFTER DELETE AS
--TRIGGER PARA CALCULAR LA NOTA MEDIA CADA VEZ QUE SE BORRE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @AntiguoIdVideojuego INT
DECLARE @Count INT

SELECT @AntiguoIdVideojuego = IdVideojuego FROM deleted

SET @Count = (SELECT COUNT(*) FROM ListaVideojuegos
	WHERE IdVideojuego = @AntiguoIdVideojuego)

SET @Media = 0

IF @Count >= 1
	BEGIN
		SELECT @Media = ISNULL(AVG(Nota),NULL) FROM ListaVideojuegos
			WHERE IdVideojuego = @AntiguoIdVideojuego
	END

	UPDATE Videojuegos
		SET NotaMedia = @Media
			WHERE Id = @AntiguoIdVideojuego
GO
CREATE OR ALTER TRIGGER Calcular_DificultadMedia ON ListaVideojuegos AFTER INSERT, UPDATE AS
--TRIGGER PARA CALCULAR LA DIFICULTAD MEDIA CADA VEZ QUE SE INSERTE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @NuevoIdVideojuego INT

SELECT @NuevoIdVideojuego = IdVideojuego FROM inserted

SELECT @Media = AVG(Dificultad) FROM ListaVideojuegos
	WHERE IdVideojuego = @NuevoIdVideojuego

	UPDATE Videojuegos
		SET DificultadMedia = @Media FROM ListaVideojuegos AS LV
		WHERE Id = @NuevoIdVideojuego
GO
CREATE OR ALTER TRIGGER Calcular_DificultadMediaBorrado ON ListaVideojuegos AFTER DELETE AS
--TRIGGER PARA CALCULAR LA DIFICULTAD MEDIA CADA VEZ QUE SE BORRE UN DATO EN LA LA TABLA DE LA LISTA DE VIDEOJUEGOS
DECLARE @Media FLOAT
DECLARE @AntiguoIdVideojuego INT
DECLARE @Count INT

SELECT @AntiguoIdVideojuego = IdVideojuego FROM inserted

SET @Count = (SELECT COUNT(*) FROM ListaVideojuegos
	WHERE IdVideojuego = @AntiguoIdVideojuego)

SET @Media = 0

IF @Count >= 1
	BEGIN
		SELECT @Media = ISNULL(AVG(Dificultad),NULL) FROM ListaVideojuegos
			WHERE IdVideojuego = @AntiguoIdVideojuego
	END

	UPDATE Videojuegos
		SET DificultadMedia = @Media
			WHERE Id = @AntiguoIdVideojuego
GO

CREATE OR ALTER TRIGGER Conteo_EstadosUsuarioInsertar ON ListaVideojuegos AFTER INSERT AS
--TRIGGER PARA HACER EL CONTEO DE CUANTOS JUEGOS HAN JUGADO/PLANEAN JUGAR/ESTAN JUGANDO, SOLO CUANDO INSERTAN EL REGISTRO ETC.
DECLARE @nuevoEstado int
DECLARE @nuevoIdUsuario int
DECLARE @conteo int

SELECT @nuevoEstado = Estado from inserted
SELECT @nuevoIdUsuario = IdUsuario from inserted
SELECT @conteo = COUNT(Estado) from ListaVideojuegos WHERE Estado = @nuevoEstado AND IdUsuario = @nuevoIdUsuario

UPDATE Usuarios SET
	VideojuegosJugados = (case when @nuevoEstado = 1 then @conteo else VideojuegosJugados end),
	VideojuegosPlaneados = (case when @nuevoEstado = 2 then @conteo else VideojuegosPlaneados end),
	VideojuegosDropeados = (case when @nuevoEstado = 3 then @conteo else VideojuegosDropeados end),
	VideojuegosEnPausa = (case when @nuevoEstado = 4 then @conteo else VideojuegosEnPausa end),
	VideojuegosJugando = (case when @nuevoEstado = 5 then @conteo else VideojuegosJugando end)
WHERE Id = @nuevoIdUsuario
GO

CREATE OR ALTER FUNCTION FN_GetConteoEstado (@Estado AS int, @IdUsuario AS int)
RETURNS int
AS
	BEGIN
		DECLARE @antiguoConteo int
		SELECT @antiguoConteo = COUNT(Estado) from ListaVideojuegos WHERE Estado = @Estado AND IdUsuario = @IdUsuario
		RETURN @antiguoConteo
	END
GO

CREATE OR ALTER TRIGGER Conteo_EstadosUsuarioActualizar ON ListaVideojuegos AFTER UPDATE AS
--TRIGGER PARA HACER EL CONTEO DE CUANTOS JUEGOS HAN JUGADO/PLANEAN JUGAR/ESTAN JUGANDO, SOLO CUANDO ACTUALIZAN EL RE ETC.
DECLARE @nuevoEstado int
DECLARE @antiguoEstado int
DECLARE @nuevoIdUsuario int
DECLARE @conteo int

SELECT @nuevoEstado = Estado from inserted
SELECT @antiguoEstado = Estado from deleted
SELECT @nuevoIdUsuario = IdUsuario from inserted
SELECT @conteo = dbo.FN_GetConteoEstado(@nuevoEstado, @nuevoIdUsuario)

/*
Podria hacer que en el set de cada campo se llamase a la funcion FN_GetConteoEstado, pero lo hago de esa manera con los case when para
que en vez de llamar a la base de datos comprobando cada campo cada vez que se actualice algo, se llame a la base de datos solo para saber el conteo
del estado anterior a la actualizacion, para lograr una mayor eficiencia
*/
UPDATE Usuarios SET
	VideojuegosJugados = (case when @nuevoEstado = 1 then @conteo when @antiguoEstado = 1 then dbo.FN_GetConteoEstado(1, @nuevoIdUsuario) else VideojuegosJugados end),
	VideojuegosPlaneados = (case when @nuevoEstado = 2 then @conteo when @antiguoEstado = 2 then dbo.FN_GetConteoEstado(2, @nuevoIdUsuario) else VideojuegosPlaneados end),
	VideojuegosDropeados = (case when @nuevoEstado = 3 then @conteo when @antiguoEstado = 3 then dbo.FN_GetConteoEstado(3, @nuevoIdUsuario) else VideojuegosDropeados end),
	VideojuegosEnPausa = (case when @nuevoEstado = 4 then @conteo when @antiguoEstado = 4 then dbo.FN_GetConteoEstado(4, @nuevoIdUsuario) else VideojuegosEnPausa end),
	VideojuegosJugando = (case when @nuevoEstado = 5 then @conteo when @antiguoEstado = 5 then dbo.FN_GetConteoEstado(5, @nuevoIdUsuario) else VideojuegosJugando end)
WHERE Id = @nuevoIdUsuario
GO



INSERT INTO Usuarios(Nickname, UserPassword, VideojuegosJugados, VideojuegosPlaneados, VideojuegosDropeados, VideojuegosEnPausa, VideojuegosJugando) VALUES('Prueba123', 'Constrasenya123', 0, 0, 0, 0, 0)
INSERT INTO Usuarios(Nickname, UserPassword, VideojuegosJugados, VideojuegosPlaneados, VideojuegosDropeados, VideojuegosEnPausa, VideojuegosJugando, esListaPrivada) VALUES('Prueba321', 'Constrasenya321', 0, 0, 0, 0, 0, 1)
INSERT INTO EstadosVideojuego(Id, NombreEstado) VALUES
	(1, 'Jugado'),
	(2, 'Planeado'),
	(3, 'Dropeado'),
	(4, 'En pausa'),
	(5, 'Jugando')

INSERT INTO Videojuegos(Nombre, Desarrollador, Distribuidores, Plataformas, FechaDeLanzamiento, Generos, urlImagen) VALUES
	('The Witcher 3', 'CD Projekt RED', 'Warner Bros, Namco Bandai Games', 'PC, XboxONE, Series X y Series Y, PS4, PS5, Nintendo Switch', '19-05-2015', 'ARPG', 'https://images.ctfassets.net/umhrp0op95v1/7rn68TUGN1lZuQRQz1zqSY/f3da676f0ce14f07653cfc5a97aad211/la-key-600.jpg'),
	('Lost Ark', 'SmileGate RPG', 'SmileGate, Amazon Games', 'PC', '11-02-2022', 'MMORPG', 'https://images.ctfassets.net/umhrp0op95v1/7rn68TUGN1lZuQRQz1zqSY/f3da676f0ce14f07653cfc5a97aad211/la-key-600.jpg'),
	('Red Dead Redemption 2', 'Rockstar Games', 'Rockstar Games', 'PC, PS4, PS5, Google Stadia, Xbox One, Xbox Series X y S', '26-10-2018', 'MMORPG', 'https://s1.gaming-cdn.com/images/products/5679/orig/red-dead-redemption-2-pc-juego-rockstar-cover.jpg'),
	('The Legend of Zelda: Breath of the Wild', 'Nintendo EPD', 'Nintendo', 'Wii U, Nintendo Switch', '03-03-2017', 'Accion-Aventura, Rol', 'https://media.vandal.net/m/43030/the-legend-of-zelda-breath-of-the-wild-201732131429_1.jpg'),
	('Viva Piñata', 'Rare', 'Xbox Game Studios', 'Xbox 360, PC, Nintendo DS', '09-11-2006', 'Life Simulation', 'https://m.media-amazon.com/images/I/81Vt4mfirVL._AC_SY500_.jpg'),
	('Pokemon Legends: Arceus', 'Game Freak', 'The Pokemon Company, Nintendo', 'Nintendo Switch', '28-01-2022', 'RPG', 'https://m.media-amazon.com/images/I/81vaoG0k9CS._AC_SY500_.jpg'),
	('NieR: Automata', 'PlatinumGames', 'Square Enix', 'PS4, PC, Xbox One', '23-02-2017', 'Rol de Acción', 'https://cdn.cloudflare.steamstatic.com/steam/apps/524220/header.jpg?t=1624266255'),
	('Undertale', 'Toby Fox', '8-4', 'PC, PS4, PSVita, Nintendo Switch, Xbox One, Series X y S', '15-09-2015', 'RPG, Puzzle', 'https://cdn.cloudflare.steamstatic.com/steam/apps/391540/header.jpg?t=1579096091'),
	('Death Stranding', 'Kojima Productions', 'Sony Interactive Entertainment', 'PC, PS4, PS5', '08-11-2019', 'Accion y exploracion', 'https://cdn.cloudflare.steamstatic.com/steam/apps/1190460/header.jpg?t=1636451066')