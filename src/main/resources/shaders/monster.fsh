#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

// a bit of portingscdf
#define iTime time
#define iMouse mouse
#define iResolution resolution

/*
	original: https://www.shadertoy.com/view/MtKyWd
*/

const int	MaxRaySteps	= 128;		// # ray steps b4 bailout
const float	MaxDist		= 3.4;		// ray distance b4 bailout
const float	FudgeFactor	= 1.;		// accuracy/speed
#define	Accuracy	2. / iResolution.x	// ray marching surface threshold
#define NormAcc		2. / iResolution.x	// surface normal accuracy

float s, c;
#define rotate(p, a) mat2(c=cos(a), s=-sin(a), -s, c) * p
void rotateXY(inout vec3 p, vec2 axy) {
    p.yz = rotate(p.yz, axy.y);
    p.xz = rotate(p.xz, axy.x);
}

vec3 fold(in vec3 p, in vec3 n) {
    n = normalize(n);
    p -= n * max(0., 2.*dot(p, n));
    return p;
}

float mapDE(in vec3 p) {
    float f;

    const float I = 64.;
    for(float i=0.; i<I; i++) {
        rotateXY(p, vec2(10.-.024273*iTime, .0045*iTime));
        //p = abs(p);

        p = fold(p, vec3(1., -1., 0.));
        p = fold(p, vec3(-1., 0., -1.));
        p -= .125*.025 / ((i+1.)/I);

    }

    f = length(p)-.007;
    return f;
}

vec3 mapSky(in vec3 p) {
    p = normalize(p);
    return
    max(
    vec3(0.),
    1.25 * vec3(1., 1.2, 1.5)
    * vec3(.5+.25*(sin(3.*p.x)+sin(3.*p.y)))
    + vec3(.4, .2, .1)
    - .25
    );
}

vec3 getNorm(vec3 p) {
    vec3 d = vec3(NormAcc, -NormAcc, 0.);
    return normalize(vec3(mapDE(p+d.xzz) - mapDE(p+d.yzz), mapDE(p+d.zxz) - mapDE(p+d.zyz), mapDE(p+d.zzx) - mapDE(p+d.zzy)));
}

// uses iq's soft shadows
/*float getShadow(vec3 hit, vec3 lightDir, float lightDist) {
	float dist;
	float k = 132.; // shadow hardness
	float totalDist = 2. / k; // starting distance based on shadow hardness
	float res = 1.;
	for(int steps=0; steps<MaxRaySteps; steps++) {
		vec3 P = hit + totalDist * lightDir;
		dist = mapDE(P);
		if(dist < Accuracy) return 0.;
        if(totalDist >= min(MaxDist, lightDist)) break;
		res = min(res, k*dist/float(steps));
		totalDist += dist;
	}
	return res;
}*/

// source lost; if this is yours, speak up :)
float getAO(vec3 hit, vec3 norm) {
    const float ns = 16.;
    float AO = 0.;
    float d = .1;
    for(float i=1.; i<ns; i++) {
        float dist = mapDE(hit+d*norm*i/ns);
        AO += .875 * ns / d * dist / i;
    }
    return clamp(AO/ns, 0., 1.);
}


// returns last and total distances from a ray traced from camPos to rayDir
vec2 march(vec3 camPos, vec3 rayDir) {
    float dist;
    float totalDist = 0.;
    vec3 p;
    for(int steps=0; steps<MaxRaySteps; steps++) {
        p = camPos + totalDist * rayDir;
        dist = mapDE(p) * FudgeFactor;
        totalDist += dist;
        if(dist<Accuracy || totalDist>MaxDist) break;
    }
    return vec2(dist, totalDist);

}

vec3 getColor(vec3 hit, vec3 rayDir, vec2 dists) {

    vec3 col = vec3(0.);

    // a surface was hit, do some shading
    if(dists.x < Accuracy) {
        vec3 norm = getNorm(hit);

        vec3 diffuse = vec3(1., .7, .5) * mapSky(norm);

        // angle of incidence
        float aoi = pow(1.-dot(norm, -rayDir), 1.);

        // ambient occlusion
        float ao = pow(getAO(hit, norm), 4.) * 2.;

        // initial color
        col = diffuse;

        // reflected sky
        vec3 ref = mapSky(normalize(reflect(rayDir, norm)));

        // mix in reflections
        col = mix(col, ref, aoi);

        // apply ao
        col *= ao;

        // mix sky into color (fog effect)
        col = mix(col, mapSky(rayDir), pow(dists.y/MaxDist, 2.));

    } else {
        // return sky only, for there's nothing else
        col = mapSky(rayDir);
    }

    return col;
}

void main( void ) {
    vec2 res = iResolution.xy;
    vec2 uv		= (gl_FragCoord.xy-.5*res) / res.y;
    vec2 mPos	= (iMouse.xy-.5*res);

    vec3 rayBeg		= vec3(0., 0., -3.);
    vec3 rayDir		= normalize(vec3(uv, 1.*2.));

    //vec2 camRotXY = 4. * mPos.xy;
    vec2 camRotXY = vec2(.75+.0353*iTime, .75+.0485*iTime);

    rotateXY(rayBeg, camRotXY);
    rotateXY(rayDir, camRotXY);

    vec2 dists	= march(rayBeg, rayDir);
    vec3 hit	= rayBeg + dists.y * rayDir;
    vec3 col = getColor(hit, rayDir, dists);

    gl_FragColor = vec4(col, 1.);
}