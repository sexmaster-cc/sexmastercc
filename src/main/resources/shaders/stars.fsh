#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 resolution;


vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 1.0, 4.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}

const float aoinParam1 = 0.5;

float snow(vec2 uv,float scale)
{
    float w=smoothstep(1000.,0.,-uv.y*(scale/200.));if(w<.1)return 0.;
    uv+=(time*aoinParam1)/scale;uv.y+=time*.03/scale;uv.x+=sin(uv.y+time*.055456)/scale;
    uv*=scale;vec2 s=floor(uv),f=fract(uv),p;float k=3.,d;
    p=.5+.35*sin(9.*fract(sin((s+p+scale)*mat2(7,3,6,5))*016.))-f;d=length(p);k=min(d,k);
    k=smoothstep(0.,k,sin(f.x+f.y)*0.01);
    return k*w;
}

void main( void ) {
    vec3 color = vec3(0.);
    vec2 uv=(gl_FragCoord.xy*.5-resolution.xy)/min(resolution.x,resolution.y);
    vec3 finalColor=vec3(0);
    float c=smoothstep(01.,0.3,clamp(uv.y*.3+.9,1.,.85));
    c+=snow(uv,30.)*.4;
    c+=snow(uv,20.)*.5;
    c+=snow(uv,15.)*.6;
    c+=snow(uv,10.)* 1.;
    c+=snow(uv,80.)*.1;
    c+=snow(uv,60.)*.3;
    c+=snow(uv,50.)*.2;
    finalColor=(vec3(c));
    gl_FragColor = (vec4( color, 1.0 ) + vec4(finalColor,1)) / vec4(2, 2, 2, 1);

}