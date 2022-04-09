using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MVGL_Entidades
{
    public class clsListaVideojuego
    {
        private int idUsuario, idVideojuego;
        private DateTime fechaDeComienzo, fechaDeFinalizacion;
        private double nota, dificultad;
        private int estado;

        public clsListaVideojuego()
        {
            this.idUsuario = 0;
            this.idVideojuego = 0;
        }

        public clsListaVideojuego(int idUsuario, int idVideojuego, DateTime fechaDeComienzo, DateTime fechaDeFinalizacion, double nota, double dificultad, int estado)
        {
            this.idUsuario = idUsuario;
            this.idVideojuego = idVideojuego;
            this.fechaDeComienzo = fechaDeComienzo;
            this.fechaDeFinalizacion = fechaDeFinalizacion;
            this.nota = nota;
            this.dificultad = dificultad;
            this.estado = estado;
        }

        public int IdUsuario { get => idUsuario; set => idUsuario = value; }
        public int IdVideojuego { get => idVideojuego; set => idVideojuego = value; }
        public DateTime FechaDeComienzo { get => fechaDeComienzo; set => fechaDeComienzo = value; }
        public DateTime FechaDeFinalizacion { get => fechaDeFinalizacion; set => fechaDeFinalizacion = value; }
        public double Nota { get => nota; set => nota = value; }
        public double Dificultad { get => dificultad; set => dificultad = value; }
        public int Estado { get => estado; set => estado = value; }
    }
}
