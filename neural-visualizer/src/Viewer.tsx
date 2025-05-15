import { useEffect, useRef } from "react"
import * as THREE from "three"

export default function Viewer() {
    const mountRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        const width = mountRef.current?.clientWidth || window.innerWidth;
        const height = mountRef.current?.clientHeight || window.innerHeight;

        const camera = new THREE.PerspectiveCamera(70, width / height, 0.1, 1000);
        camera.position.z = 5;

        const scene = new THREE.Scene();

        const inputNodes = [
            new THREE.Vector3(-2, 1, 0),
            new THREE.Vector3(-2, 0, 0),
            new THREE.Vector3(-2, -1, 0)
        ];
        const outputNodes = [
            new THREE.Vector3(2, 0.5, 0),
            new THREE.Vector3(2, -0.5, 0)
        ];

        const geometry = new THREE.SphereGeometry(0.2, 32, 32);
        const material = new THREE.MeshNormalMaterial();

        inputNodes.forEach((pos) => {
            const mesh = new THREE.Mesh(geometry, material);
            mesh.position.copy(pos);
            scene.add(mesh);
        });

        outputNodes.forEach((pos) => {
            const mesh = new THREE.Mesh(geometry, material);
            mesh.position.copy(pos);
            scene.add(mesh);
        });

        const edgeMaterial = new THREE.LineBasicMaterial({ color: 0x888888 });
        inputNodes.forEach((inputPos) => {
            outputNodes.forEach((outputPos) => {
                const points = [inputPos, outputPos];
                const edgeGeometry = new THREE.BufferGeometry().setFromPoints(points);
                const line = new THREE.Line(edgeGeometry, edgeMaterial);
                scene.add(line);
            });
        });

        const renderer = new THREE.WebGLRenderer({ antialias: true });
        renderer.setSize(width, height);

        renderer.setAnimationLoop(() => {
            renderer.render(scene, camera);
        });
        mountRef.current?.appendChild(renderer.domElement);
    }, []);

    return (
        <div ref={mountRef} style={{ width: '100vw', height: '100vh' }}></div>
    )
}
