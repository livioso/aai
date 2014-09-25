/**********************************************************************/
/*  File - FuzzyFan.c                                                 */
/*                                                                    */
/*  Description: This file contains the code for a fuzzy logic        */
/*  expert fan controller.                                            */
/*                                                                    */
/**********************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* The following defines the data sets used for 
** the Fuzzy Logic Expert FAN Controller
**
**      T[] - universal discourse for temperature
**   COOL[] - temperatures between 40 to 80
**   WARM[] - temperatures between 60 to 100
**    HOT[] - temperatures between 80 to 120
**
**      H[] - universal discourse for humidity
**    DRY[] - humidity between 20 to 60
**  MOIST[] - humidity between 40 to 80
**    WET[] - humidity between 60 to 100
**
**     FS[] - universal discourse for fan speed
**    LOW[] - fan speed between 250 to 750
** MEDIUM[] - fan speed between 500 to 1000
**   HIGH[] - fan speed between 750 to 1250
**
**   ZERO[] - contains all 0 values..
**
**  Note: The fuzzy sets are linearly defined over the full
**  universe of discourse. Even though only 5 elements are 
**  defined, values between this elements are computed..
**
**                     Temperature
**                 ______            ______
**                |      \    /\    /      |
**                |       \  /  \  /       |
**                |  cool  \/warm\/   hot  |
**                |        /\    /\        |
**                |       /  \  /  \       |
**                |      /    \/    \      |
**                --------------------------
**                40    60    80    100   120
**
*/                             

#define N_ELEM 5         /* N_ELEM defines the number */
                         /* of fuzzy set values       */
float     T[N_ELEM] = {  40,   60,   80,  100,  120};
float  COOL[N_ELEM] = { 1.0,  1.0,    0,    0,    0};
float  WARM[N_ELEM] = {   0,    0,  1.0,    0,    0};
float   HOT[N_ELEM] = {   0,    0,    0,  1.0,  1.0};

float     H[N_ELEM] = {  20,   40,   60,   80,  100};
float   DRY[N_ELEM] = { 1.0,  1.0,    0,    0,    0};
float MOIST[N_ELEM] = {   0,    0,  1.0,    0,    0};
float   WET[N_ELEM] = {   0,    0,    0,  1.0,  1.0};

float    FS[N_ELEM] = { 250,  500,  750, 1000, 1250};
float   LOW[N_ELEM] = { 1.0,  1.0,    0,    0,    0};
float   MED[N_ELEM] = {   0,    0,  1.0,    0,    0};
float  HIGH[N_ELEM] = {   0,    0,    0,  1.0,  1.0};

float  ZERO[N_ELEM] = {   0,    0,    0,    0,    0};


/* GetMembership()
**
** GetMembership() is used to calculate the membership value Ao
** with respect to a given Xo value based on the universe X[]
** and the fuzzy set A[]. The calculated value is returned.
**
** Designed by: Henry Hurdon      Date: Mar.  15, 1993
** Reviewed by: ................. Date: ..............
** Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
float GetMembership(float xo, float X[], float A[])
{
  float ao;
  int si;

  /* error check bounds on xo..
   * if xo outside of universe X[N_ELEM] then
   * return closest membership value. */
  if (xo < X[0]) {
     return(A[0]);
  } else if (xo > X[N_ELEM -1]) {
    return(A[N_ELEM -1]);
  }

  /* Ok, calculate the membership value */
  for (si = 0; si < N_ELEM-1; si++) {
    /* calculate membership value */
    if ((xo >= X[si])&&(xo <= X[si+1])) {
      if (X[si] == X[si+1]) {
         /* membership value based on maximum value */
         ao = max(A[si], A[si+1]);
      } else {
         /* membership value based on linear interpolation */
         ao = (xo-X[si]) * ((A[si+1]-A[si])/(X[si+1]-X[si])) + A[si];
      }
    }
  }

  return(ao);
}


/* GetMaximum()
**
** GetMaximum() is used to perform the MAX operation on the fuzzy sets
** A[] and B[]. The results are stored in C[].
**
** Designed by: Henry Hurdon      Date: Mar.  15, 1993
** Reviewed by: ................. Date: ..............
** Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void GetMaximum(float A[N_ELEM], float B[N_ELEM], float C[N_ELEM])
{
  int   si;

  for (si = 0; si < N_ELEM; si++) {
    C[si] = max(A[si], B[si]);
  }
}


/* DoInferEngine()
**
** DoInferEngine() is used to perform the inference engine calculation
** based on Ao and Bo and C[]. The results are stored in O[].
**
** Designed by: Henry Hurdon      Date: Mar.  15, 1993
** Reviewed by: ................. Date: ..............
** Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void DoInferEngine(float Wo, float C[], float O[])
{
  int   si;

  for (si = 0; si < N_ELEM; si++) {
    O[si] = Wo*C[si];
  }
}


/* DeFuzzyOutput()
**
** DeFuzzyOutput() is used to de-fuzzify the universe Y[] based on 
** the fuzzy set B[], using the center of gravity theorem.
** The result is stored in *Yo.
**
** Designed by: Henry Hurdon      Date: Mar.  15, 1993
** Reviewed by: ................. Date: ..............
** Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void DeFuzzyOutput(float Y[N_ELEM], float B[N_ELEM], float *Yo)
{
  float yB_sum, B_sum;
  int   si;

  yB_sum = 0.0;
  B_sum  = 0.0;

  /* get summations of Y[]B[] and B[] */
  for (si = 0; si < (N_ELEM -1); si++) {
    yB_sum += ((Y[si] + Y[si+1])/2)*((B[si] + B[si+1])/2);
     B_sum += (B[si] + B[si+1])/2;
  }

  /* check for divide by zero */
  if (B_sum != 0.0) {
    *Yo = yB_sum/B_sum;
  }
}


/* The following functions are found in the VGAHI.C or the DATA.C file,
** and are used to simulate the controller interface.     */
extern void InitController(void );
extern void GetInputs(float *To, float *Ho);
extern void SetOutput(float FSo);

/* The following variables are declared globally, so that they can be used
** by the display functions in VGAHI.C */
float R[N_ELEM];                  /* R[] - interm inference result        */
float O[N_ELEM];                  /* O[] - accumulative inference result  */


/* main()
**
** main() is the main procedure for the "Fuzzy Logic Expert Fan Controller".
**
** The following contains the linguistic matrix used for the FuzzyFan
** controller. Notice that the ideal condition is "warm and dry". If the
** room is cool the fan can be used to draw "warm and dry" air from outside
** the room. Likewise, if the room is hot and/or wet/moist, the fan can be
** used to cool/dry the room by expelling the hot/wet/moist air, drawing in
** the "warm and dry" air from outside the room.
**
**
**                     Temperature
**
**                 DRY    MOIST    WET                
**              +-------+-------+-------+
**         COOL |  MED  | HIGH  | HIGH  |
**              +-------+-------+-------+
**  Temp.  WARM |  LOW  |  MED  | HIGH  |
**              +-------+-------+-------+
**          HOT |  MED  | HIGH  | HIGH  |
**              +-------+-------+-------+
**
** The above linguistic matrix can be reduced to the following fuzzy rules.
**
**     IF H = WET  THEN FS = HIGH               
**     IF T = COOL AND H = DRY   THEN FS = MED   
**     IF T = COOL AND H = MOIST THEN FS = HIGH
**     IF T = WARM AND H = DRY   THEN FS = LOW   
**     IF T = WARM AND H = MOIST THEN FS = MED 
**     IF T = HOT  AND H = DRY   THEN FS = MED    
**     IF T = HOT  AND H = MOIST THEN FS = HIGH 
**
** Designed by: Henry Hurdon      Date: Mar.  15, 1993
** Reviewed by: ................. Date: ..............
** Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void main()
{
  float To, Ho, FSo;              /* crisp input (To, Ho) and outut (FSo) */
                                  /* To  - crisp temperature              */
                                  /* Ho  - crisp humidity                 */
                                  /* FSo - crisp fanspeed                 */

  float T_COOL, T_WARM,  T_HOT;   /* membership values for To   */
  float H_DRY,  H_MOIST, H_WET;   /* membership values for Ho   */
  float FS_LOW, FS_MED,  FS_HIGH; /* membership values for FSo  */


  /* Initialize the controller */
  InitController();

  /* loop forever */
  while (1) {

    /* get real world inputs */
    GetInputs(&To, &Ho);
    
    /* convert to membership values.. */
    H_WET   = GetMembership(Ho, H, WET);
    H_MOIST = GetMembership(Ho, H, MOIST);
    H_DRY   = GetMembership(Ho, H, DRY);

    T_COOL  = GetMembership(To, T, COOL);
    T_WARM  = GetMembership(To, T, WARM);
    T_HOT   = GetMembership(To, T, HOT);

    /* initialize output membership values.. */
    FS_LOW  = 0.0;
    FS_MED  = 0.0;
    FS_HIGH = 0.0;


    /* The following executes the rule base used for
    *  the Fuzzy Logic Expert FAN Controller. This algorithm
    * is based on the "max product" implication. */

    /* IF H = WET THEN FS = HIGH                 */
    FS_HIGH = max(H_WET,          FS_HIGH);

    /* IF T = COOL AND H = DRY THEN FS = MED     */
    FS_MED  = max(T_COOL*H_DRY,   FS_MED);

    /* IF T = COOL AND H = MOIST THEN FS = HIGH  */
    FS_HIGH = max(T_COOL*H_MOIST, FS_HIGH);

    /* IF T = WARM AND H = DRY THEN FS = LOW     */
    FS_LOW  = max(T_WARM*H_DRY,   FS_LOW);

    /* IF T = WARM AND H = MOIST THEN FS = MED   */
    FS_MED  = max(T_WARM*H_MOIST, FS_MED);

    /* IF T = HOT AND H = DRY THEN FS = MED      */
    FS_MED  = max(T_HOT*H_DRY,    FS_MED);
       
    /* IF T = HOT AND H = MOIST THEN FS = HIGH   */
    FS_HIGH = max(T_HOT*H_MOIST,  FS_HIGH);


    /* Calculate inferences.. */
    DoInferEngine(FS_LOW,  LOW, R);
    GetMaximum(R, ZERO, O);

    DoInferEngine(FS_MED,  MED, R);
    GetMaximum(R, O, O);

    DoInferEngine(FS_HIGH, HIGH, R);
    GetMaximum(R, O, O);

    /* Ok.. defuzzify and output crisp result */
    DeFuzzyOutput(FS, O, &FSo);
    SetOutput(FSo);
  }
}
-- FuzzyFan.c - END HERE -- FuzzyFan.c - END HERE -- FuzzyFan.c - END HERE -- 



-- VGAHI.c    - CUT HERE -- VGAHI.c    - CUT HERE -- VGAHI.c    - CUT HERE -- 
/***************************************************************************/
/*                                                                         */
/* File: VGAHI.C                                                           */
/*                                                                         */
/* Description: This file contains the VGAHI graphics routines used to     */
/*              display and simulate the Fuzzy controller.                 */
/*                                                                         */
/***************************************************************************/
#include <stdio.h>
#include <conio.h>
#include <stdlib.h>
#include <graphics.h>
#include <math.h>
#include <mem.h>

/* The following variables are defined in FuzzyFan.c */
#define N_SET 5
extern float T[];
extern float COOL[];
extern float WARM[];
extern float HOT[];

extern float H[];
extern float DRY[];
extern float MOIST[];
extern float WET[];

extern float FS[];
extern float LOW[];
extern float MED[];
extern float HIGH[];

extern float O[];

extern float GetMembership(float Xo, float X[], float A[]);


/* ggetch()
*
*  ggetch is a function that is used to for echoless keyboard entry
*  including CURSOR and FUNCTION keys. Returns int ch.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1990
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1990
*/

/* defines for special chars */
#define HOME            199
#define END             207
#define PGUP            201
#define PGDN            209
#define LEFT            203
#define RIGHT           205
#define UP              200
#define DOWN            208
#define INS             210
#define DEL             211
#define CTRL_HOME       247
#define CTRL_END        245

#define F1              187
#define F2              188
#define F3              189
#define F4              190
#define F5              191
#define F6              192
#define F7              193
#define F8              194
#define F9              195
#define F10             196

int ggetch(void)
{
  int ch;

  ch = getch();

  if ((ch == 0x00) || (ch == 0xE0)) {
    ch = getch() | 0x80;
  }
  ch &= 0xFF;

  return (ch);
}


/* InitGraphics(void)
*
*  InitGraphics is used to initialize the graphics library.
*  
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void InitGraphics(void)
{
  int GraphDriver, GraphMode, GraphError;

  /* initialize graphics system */
  GraphDriver = VGA;
  GraphMode   = VGAHI;
  initgraph(&GraphDriver, &GraphMode, "");

  /* test result of initialization */
  GraphError = graphresult();
  if(GraphError != grOk) {
   printf("FUZZYFAN graphics system error: %s\n", grapherrormsg(GraphError));
   exit(1);
  }
}


/* QuitGraphics(void)
*
*  QuitGraphics is used to reset the screen to it's mode prior to
*  calling InitGraphics.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
static void QuitGraphics(void)
{
  cleardevice();      /* clear graphics screen  */
  closegraph();
}


/* DrawGraph()
*
*  DrawGraph is used to initialize a graph plot area on the vga screen.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
#define GRAPH_WIDTH   319
#define GRAPH_HEIGHT  150
#define GRAPH_LEFT     32
#define GRAPH_BOTTOM  130

void DrawGraph(int x_pos, int y_pos,
               char *name,  char *str1,  char *str2, char *str3)
{
  struct viewporttype viewport;
  int    si;

  /* save current viewport settings */
  getviewsettings(&viewport);

  /* clear viewport. */
  setviewport(x_pos, y_pos, x_pos +GRAPH_WIDTH, y_pos +GRAPH_HEIGHT, 1);
  clearviewport();

  /* display title on screen */
  setcolor(WHITE);
  settextjustify(CENTER_TEXT, CENTER_TEXT);
  outtextxy(GRAPH_WIDTH/2,   10, name);
  outtextxy(60,              20, str1);
  outtextxy(GRAPH_WIDTH/2,   20, str2);
  outtextxy(GRAPH_WIDTH -60, 20, str3);

  /* draw graph axis */
  setcolor(YELLOW);
  line(GRAPH_LEFT,     GRAPH_BOTTOM, GRAPH_LEFT,      GRAPH_BOTTOM -105);
  line(GRAPH_LEFT,     GRAPH_BOTTOM, GRAPH_LEFT +256, GRAPH_BOTTOM);
  line(GRAPH_LEFT+256, GRAPH_BOTTOM, GRAPH_LEFT +256, GRAPH_BOTTOM -105);

  /* display Y-axis values */
  outtextxy(GRAPH_LEFT -17, GRAPH_BOTTOM -100, "1.0");
  outtextxy(GRAPH_LEFT -17, GRAPH_BOTTOM - 50, "0.5");
  outtextxy(GRAPH_LEFT -17, GRAPH_BOTTOM,      "0");

  /* restore current viewport settings */
  setviewport(viewport.left, viewport.top,
              viewport.right, viewport.bottom, viewport.clip);
}


/* PlotGraph()
*
*  PlotGraph is used to plot a fuzzy set A[] in the graph plot area on
*  the vga screen.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
#define W_SET  256
void PlotGraph(int x_pos, int y_pos, float X[], float A[], int color)
{
  struct viewporttype viewport;
  int    si;
  float x, xinc, a[W_SET];

  /* Transform the universe X[] and fuzzy set A[]
   * into a universe x = {0,.. 255} having a fuzzy set a[W_SET]. */
  x     = X[0];
  xinc  =(X[N_SET-1]-X[0])/W_SET;

  for(si = 0; si < W_SET; si++) {
    a[si] = GetMembership(x, X, A);
    x    += xinc;
  }

  /* save current viewport settings */
  getviewsettings(&viewport);

  /* Ok.. draw the plot of a[W_SET] */
  setviewport(x_pos, y_pos, x_pos +GRAPH_WIDTH, y_pos +GRAPH_HEIGHT, 1);

  setwritemode(XOR_PUT);
  setcolor(color);
  for(si = 0; si < W_SET; si++) {
     if (a[si] > 0) {
       line(GRAPH_LEFT+si, GRAPH_BOTTOM -((int )(100*a[si])),
            GRAPH_LEFT+si, GRAPH_BOTTOM -((int )(100*a[si])));
     }
  }
  setwritemode(COPY_PUT);

  /* restore current viewport settings */
  setviewport(viewport.left, viewport.top,
              viewport.right, viewport.bottom, viewport.clip);
}

/* PlotSingleTon()
*
*  PlotSingleTon is used to plot a singleton value Ao onto the fuzzy set A[]
*  which was previously plotted in the graph plot area on the vga screen.
*
*  Note: The PlotSingleTon routine uses an setwritemode(XOR_PUT) to draw
*  graphics to the screen.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void PlotSingleTon(int x_pos, int y_pos, float Xo, float X[])
{
  struct viewporttype viewport;
  int    si;
  float x, xinc;

  /* calculate offset for singleton */
  xinc  = (X[N_SET-1]-X[0])/W_SET;
  x     = (Xo - X[0])/xinc;

  si    = (int )x;

  /* Ok.. draw the singleton */
  /* save current viewport settings */
  getviewsettings(&viewport);

  setviewport(x_pos, y_pos, x_pos +GRAPH_WIDTH, y_pos +GRAPH_HEIGHT, 1);

  setwritemode(XOR_PUT);
  setcolor(WHITE);
  line(GRAPH_LEFT+si, GRAPH_BOTTOM,
       GRAPH_LEFT+si, GRAPH_BOTTOM -((int )(100*1.0)));
  setwritemode(COPY_PUT);

  /* restore current viewport settings */
  setviewport(viewport.left, viewport.top,
              viewport.right, viewport.bottom, viewport.clip);
}


/* DrawBar()
*
*  DrawBar is used to initialize a bar plot area on the vga screen.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
#define BAR_WIDTH    80
#define BAR_HEIGHT  145
#define BAR_LEFT     40
#define BAR_BOTTOM  125

void DrawBar(int x_pos, int y_pos, char *name)
{
  struct viewporttype viewport;
  int    si;

  /* save current viewport settings */
  getviewsettings(&viewport);

  /* clear viewport. */
  setviewport(x_pos, y_pos, x_pos +BAR_WIDTH, y_pos +BAR_HEIGHT, 1);
  clearviewport();

  /* draw border around Bar */
  setcolor(LIGHTRED);
  rectangle(0, 0, BAR_WIDTH, BAR_HEIGHT);

  /* display title on screen */
  setcolor(WHITE);
  settextjustify(CENTER_TEXT, CENTER_TEXT);
  outtextxy(BAR_WIDTH/2, BAR_BOTTOM +15, name);

  /* display Y-axis values */
  outtextxy(BAR_LEFT -20, BAR_BOTTOM -100, "100%");
  outtextxy(BAR_LEFT -20, BAR_BOTTOM - 75, " 75%");
  outtextxy(BAR_LEFT -20, BAR_BOTTOM - 50, " 50%");
  outtextxy(BAR_LEFT -20, BAR_BOTTOM - 25, " 25%");
  outtextxy(BAR_LEFT -20, BAR_BOTTOM,      "  0%");

  /* restore current viewport settings */
  setviewport(viewport.left, viewport.top,
              viewport.right, viewport.bottom, viewport.clip);
}


/* PlotBar()
*
*  PlotBar is used to plot a fuzzy set A[] in the Bar plot area on
*  the vga screen.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
#define W_SET  256
void PlotBar(int x_pos, int y_pos, float Xo, float X[], int color)
{
  struct viewporttype viewport;
  int    si;
  char   buffer[20];
  float x, xinc;

  /* calculate offset for singleton */
  xinc  = (X[N_SET-1]-X[0])/100.0;
  x     = (Xo - X[0])/xinc;

  si    = (int )x;

  /* Ok.. draw the singleton */
  /* save current viewport settings */
  getviewsettings(&viewport);

  /* Ok.. draw the plot of a[W_SET] */
  setviewport(x_pos, y_pos, x_pos +BAR_WIDTH, y_pos +BAR_HEIGHT, 1);

  setfillstyle(SOLID_FILL, BLACK);
  bar(BAR_LEFT, BAR_BOTTOM - 120, BAR_LEFT+30, BAR_BOTTOM -si);

  setfillstyle(SOLID_FILL, color);
  bar(BAR_LEFT, BAR_BOTTOM, BAR_LEFT+30, BAR_BOTTOM -si);
  outtextxy(BAR_LEFT +15, BAR_BOTTOM - (si +10), itoa((int )Xo, buffer, 10));

  /* restore current viewport settings */
  setviewport(viewport.left, viewport.top,
              viewport.right, viewport.bottom, viewport.clip);
}


/* InitController()
*
*  InitController() is used to simulate the startup routines that would
*  be performed if this was an actual controller. In this case, it simply
*  initializes the VGA display and sets up the user interface.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
float  temperature =  72.0;
float  humidity    =  45.0;
float  fanspeed    = 250.0;

void InitController(void)
{
   InitGraphics();

   outtextxy(20, 350, "Fuzzy Fan Controller");
   outtextxy(20, 370, "Increase Temperature - CURSOR UP");
   outtextxy(20, 380, "Decrease Temperature - CURSOR DOWN");
   outtextxy(20, 390, "Increase Humidity    - CURSOR RIGHT");
   outtextxy(20, 400, "Decrease Humidity    - CURSOR LEFT");
   outtextxy(20, 410, "Quit program         - Q,q");

   DrawGraph(319,   0, "Temperature (F)", "COOL", "WARM", "HOT");
   PlotGraph(319,   0, T, COOL,  LIGHTGREEN);
   PlotGraph(319,   0, T, WARM,  LIGHTCYAN);
   PlotGraph(319,   0, T, HOT,   LIGHTRED);
   PlotSingleTon(319,   0, temperature,  T);

   DrawGraph(319, 150, "Humidity (%)", "DRY", "MOIST", "WET");
   PlotGraph(319, 150, H, DRY,   LIGHTGREEN);
   PlotGraph(319, 150, H, MOIST, LIGHTCYAN);
   PlotGraph(319, 150, H, WET,   LIGHTRED);
   PlotSingleTon(319,  150, humidity,  H);

   DrawGraph(319, 300, "FanSpeed (rpm)", "LOW", "MED", "HIGH");
   PlotGraph(319, 300, FS, LOW,  LIGHTGREEN);
   PlotGraph(319, 300, FS, MED,  LIGHTCYAN);
   PlotGraph(319, 300, FS, HIGH, LIGHTRED);

   DrawBar(10,  170, "T");
   PlotBar(10,  170, temperature, T,  LIGHTGREEN);

   DrawBar(110, 170, "H");
   PlotBar(110, 170, humidity,    H,  LIGHTCYAN);

   DrawBar(210, 170, "FS");
   PlotBar(210, 170, fanspeed,    FS, YELLOW);

   DrawGraph(0,   0, "Fuzzy FanSpeed (rpm)", "LOW", "MED", "HIGH");
}

/* GetInputs()
*
*  GetInputs() is used to simulate the reading of the input variables for
*  the fuzzy logic controller.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void GetInputs(float *Uo, float *Vo)
{
   *Uo = temperature;
   *Vo = humidity;
}

/* SetOutput()
*
*  SetOutput() is used to simulate the setting of the output variable for
*  the fuzzy logic controller. In this case however, it updates the
*  user display and retrieves new input.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
#define INC  1.0
void SetOutput(float Yo)
{
   int ch;

   /* plot new fanspeed */
   fanspeed = Yo;
   PlotGraph(0, 0, FS, O,    WHITE);
   PlotSingleTon(  0, 0,    fanspeed, FS);
   PlotSingleTon(319, 300,    fanspeed, FS);
   PlotBar(210,  170, fanspeed, FS,  YELLOW);

   /* Get knew information */
   ch = ggetch();

   switch (ch) {
   case 'Q':
   case 'q':
     QuitGraphics();
     exit(0);
     break;

   case UP:
     /* remove old temperature */
     PlotSingleTon(319, 0, temperature, T);

     if (temperature < T[N_SET-1]) {
        temperature += INC;
     }
     if (temperature > T[N_SET-1]) {
        temperature = T[N_SET-1];
     }
     PlotSingleTon(319, 0, temperature, T);
     PlotBar(10, 170, temperature, T,  LIGHTGREEN);
     break;

   case DOWN:
     /* remove old temperature */
     PlotSingleTon(319, 0, temperature, T);

     if (temperature > T[0]) {
        temperature -= INC;
     }
     if (temperature < T[0]) {
        temperature = T[0];
     }
     PlotSingleTon(319, 0, temperature, T);
     PlotBar(10, 170, temperature, T,  LIGHTGREEN);
     break;

   case RIGHT:
     /* remove old humidity */
     PlotSingleTon(319, 150, humidity,  H);

     if (humidity < H[N_SET -1]) {
        humidity += INC;
     }
     if (humidity > H[N_SET -1]) {
        humidity = H[N_SET -1];
     }
     PlotSingleTon(319, 150, humidity, H);
     PlotBar(110,  170, humidity, H,  LIGHTCYAN);
     break;

   case LEFT:
     /* remove old humidity */
     PlotSingleTon(319, 150, humidity, H);

     if (humidity > H[0]) {
        humidity -= INC;
     }
     if (humidity < H[0]) {
        humidity = H[0];
     }
     PlotSingleTon(319, 150, humidity, H);
     PlotBar(110, 170, humidity, H, LIGHTCYAN);
     break;
   }

   /* remove old fanspeed */
   PlotGraph(0, 0, FS, O,    WHITE);
   PlotSingleTon(  0, 0,    fanspeed, FS);
   PlotSingleTon(319, 300,    fanspeed, FS);
}
-- VGAHI.c    - END HERE -- VGAHI.c    - END HERE -- VGAHI.c    - END HERE -- 




-- FuzzyPrn.c - CUT HERE -- FuzzyPrn.c - CUT HERE -- FuzzyPrn.c - CUT HERE -- 
/*****************************************************************************/
/*  File - FuzzyPrn.c                                                            */
/*                                                                           */
/*  Description: This file contains the code for driving a fuzzy logic       */
/*  expert fan controller.                                                   */
/*                                                                           */
/*  This program will generate a 2-dimensional array of numbers,             */
/*  representing the control surface.. This surface may be displayed using   */
/*  the 'mesh' command in MatLab..                                           */
/*                                                                           */
/*****************************************************************************/
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

/* The following defines the data sets used for 
*  driving the Fuzzy Logic Expert FAN Controller
*
*      T[] - universal discourse for temperature
*      H[] - universal discourse for humidity
*/
#define N_SET 17           /* N_SET defines the number of fuzzy set values   */
float t[]={40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95,100,105,110,115,120};
float h[]={20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95,100};

/* InitController()
*
*  InitController() is used to simulate the startup routines that would
*  be performed if this was an actual controller.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
int h_inc = 0;
int t_inc = 0;

void InitController(void)
{
 /* empty "stub" procedure */
}


/* GetInputs()
*
*  GetInputs() is used to simulate the reading of the input variables for
*  the fuzzy logic controller.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void GetInputs(float *Uo, float *Vo)
{
   *Uo = t[t_inc];
   *Vo = h[h_inc];
}

/* SetOutput()
*
*  SetOutput() is used to simulate the setting of the output variable for
*  the fuzzy logic controller.
*
*  Designed by: Henry Hurdon      Date: Mar.  15, 1993
*  Reviewed by: ................. Date: ..............
*  Modified by: Henry Hurdon      Date: Mar.  15, 1993
*/
void SetOutput(float Yo)
{
   if (h_inc == (N_SET-1)) {
      printf("% 8.2f\n", Yo);
      h_inc = 0;

      if (t_inc == (N_SET-1)) {
        exit(0);
      } else {
        t_inc++;
      }
   } else {
      printf("% 8.2f", Yo);
      h_inc++;
   }
}
-- FuzzyPrn.c - END HERE -- FuzzyPrn.c - END HERE -- FuzzyPrn.c - END HERE -- 
