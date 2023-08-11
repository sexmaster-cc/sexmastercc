
#ifdef GL_ES
precision highp float;
#endif



#extension GL_OES_standard_derivatives : enable

uniform float time;
uniform vec2 resolution;

#define PI 3.141519
#define TAU 6.283185

// mercury sdf
// Repeat around the origin by a fixed angle.
// For easier use, num of repetitions is use to specify the angle.
float pModPolar(inout vec2 p, float repetitions)
{
	float angle = 2.0*PI/repetitions;
	float a = atan(p.y, p.x) + angle/2.;
	float r = length(p);
	float c = floor(a/angle);
	a = mod(a,angle) - angle/2.;
	p = vec2(cos(a), sin(a))*r;
	// For an odd number of repetitions, fix cell index of the cell in -x direction
	// (cell index would be e.g. -5 and 5 in the two halves of the cell):
	if (abs(c) >= (repetitions/2.0)) c = abs(c);
	return c;
}

vec2 rot(vec2 v, float angle)
{
    float c = cos(angle);
    float s = sin(angle);
    return mat2(c, -s, s, c)*v;
}

float pMod1(inout float p, float size)
{
	float halfsize = size*0.5;
	float c = floor((p + halfsize)/size);
	p = mod(p + halfsize, size) - halfsize;
	return c;
}

float rand(in vec2 st)
{
    return fract(sin(dot(st,vec2(24.9898,8.233))) * 444.54531);
}

float rect(vec2 p, vec2 c, vec2 rad)
{
    vec2 d = abs(p - c) - rad;
    return max(d.x, d.y);
}

float sub(float a, float b)
{
    return max(a, -b);
}

float Hitler(vec2 p,float s)
{
    float sd = rect(p, vec2(0.0), vec2(0.7)*s);
    sd = sub(sd, rect(p, vec2(-0.3, +0.5)*s, vec2(0.2, 0.35)*s));
    sd = sub(sd, rect(p, vec2(+0.3, -0.5)*s, vec2(0.2, 0.35)*s));
    sd = sub(sd, rect(p, vec2(-0.5, -0.3)*s, vec2(0.35, 0.2)*s));
    sd = sub(sd, rect(p, vec2(+0.5, +0.3)*s, vec2(0.35, 0.2)*s));
    return sd;
}

float Nazi( in vec2 p, float a, float s )
{
    p = rot(p,a);
    return Hitler(p,s);
}

vec3 background()
{
	vec3 col0 = vec3(0.5,0.1,0.1);
	vec3 col1 = vec3(0.0);
	vec2 uv = (gl_FragCoord.xy *2.0 - resolution.xy)  / resolution.y;
	float dd = length(uv);
	float rv = sin(time*0.4+dd*0.1);
	uv = rot(uv,-rv);
	float c = pModPolar(uv,21.0);
	uv.x -= time*0.2;
	float c1 = pMod1(uv.x,0.4);
	float rr = rand(vec2(c1,c));
	float size = clamp(0.116*dd*rr,0.035,0.3);
	float d = Nazi(uv, rr*TAU-time*0.8, size);
	vec3 col = mix( col0*0.7,col1, smoothstep(0.0,0.002,d) )*dd;
	return col;

}

float map( in vec3 pos )
{
    vec2 p = pos.xy;
    p = rot(p,time*0.25+0.5);
    float d = Hitler(p,0.6);
    float dep = 0.035;
    vec2 e = vec2( d, abs(pos.z) - dep );
    d = min(max(e.x,e.y),0.0) + length(max(e,0.0));
    return d-0.035;
}

// http://iquilezles.org/www/articles/normalsSDF/normalsSDF.htm
vec3 calcNormal( in vec3 pos )
{
    vec2 e = vec2(1.0,-1.0)*0.5773;
    const float eps = 0.0005;
    return normalize( e.xyy*map( pos + e.xyy*eps ) +
					  e.yyx*map( pos + e.yyx*eps ) +
					  e.yxy*map( pos + e.yxy*eps ) +
					  e.xxx*map( pos + e.xxx*eps ) );
}

vec3 render( vec2 p )
{

     // camera movement
    float an = 0.5*(time-10.0);
    vec3 ro = vec3( 2.0*cos(an), -.8, 2.0*sin(an) );
    vec3 ta = vec3( 0.0, 0.0, 0.0 );
    // camera matrix
    vec3 ww = normalize( ta - ro );
    vec3 uu = normalize( cross(ww,vec3(0.0,1.0,0.0) ) );
    vec3 vv = normalize( cross(uu,ww));

    // create view ray
    vec3 rd = normalize( p.x*uu + p.y*vv + 1.5*ww );

    // raymarch
    const float tmax = 3.0;
    float t = 0.0;
    for( int i=0; i<100; i++ )
    {
        vec3 pos = ro + t*rd;
        float h = map(pos);
        if( h<0.0001 || t>tmax ) break;
        t += h;
    }

    // shading/lighting
    vec3 col = vec3(0.0);
    if( t<tmax )
    {
        vec3 pos = ro + t*rd;
        vec3 nor = calcNormal(pos);
        float dif = clamp( dot(nor,vec3(0.57703)), 0.0, 1.0 );
        float amb = 0.5 + 0.5*dot(nor,vec3(0.0,1.0,0.0));
        col = vec3(0.2,0.3,0.4)*amb + vec3(0.8,0.7,0.5)*dif;
    }

    // gamma
    return sqrt( col );
}

void main( void )
{
	vec2 p = (-resolution.xy + 2.0*gl_FragCoord.xy)/resolution.y;
	p.x += sin(time*0.3);
	p.y += cos(time*0.3);
	float c = pMod1(p.y,1.0);
	p.x += c*0.5;
	float c1 = pMod1(p.x,1.0);
	vec3 col = render(p);
	col.gb *= (1.0-abs(c1)*0.2);
	col = mix(background(), col,1.0-step(length(col),0.0));
	gl_FragColor = vec4( col, 1.0 );
}
