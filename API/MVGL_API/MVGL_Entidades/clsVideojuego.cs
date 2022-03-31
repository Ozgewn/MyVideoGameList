using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_Entidades
{
    public class clsVideojuego
    {
        private int id;
        private string nombre, desarrollador, distribuidores, plataformas, generos, urlImagen;
        private double notaMedia, dificultadMedia;
        private DateTime fechaDeLanzamiento;

        public clsVideojuego()
        {
            this.id = 0;
        }

        public clsVideojuego(int id, string nombre, string desarrollador, string distribuidores, string plataformas, double notaMedia, double dificultadMedia, DateTime fechaDeLanzamiento, string generos, string urlImagen)
        {
            this.id = id;
            this.nombre = nombre;
            this.desarrollador = desarrollador;
            this.distribuidores = distribuidores;
            this.plataformas = plataformas;
            this.notaMedia = notaMedia;
            this.dificultadMedia = dificultadMedia;
            this.fechaDeLanzamiento = fechaDeLanzamiento;
            this.generos = generos;
            this.urlImagen = urlImagen;
        }

        public int Id { get => id; set => id = value; }
        public string Nombre { get => nombre; set => nombre = value; }
        public string Desarrollador { get => desarrollador; set => desarrollador = value; }
        public string Distribuidores { get => distribuidores; set => distribuidores = value; }
        public string Plataformas { get => plataformas; set => plataformas = value; }
        public double NotaMedia { get => notaMedia; set => notaMedia = value; }
        public double DificultadMedia { get => dificultadMedia; set => dificultadMedia = value; }
        public DateTime FechaDeLanzamiento { get => fechaDeLanzamiento; set => fechaDeLanzamiento = value; }
        public string Generos { get => generos; set => generos = value; }
        public string UrlImagen { get => urlImagen; set => urlImagen = value; }
    }
}
