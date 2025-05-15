import { useEffect } from "react"
import Viewer from "./Viewer"

export default function App() {
    useEffect(() => {
        const ws = new WebSocket("ws://localhost:6666");

        ws.onmessage = (e) => {
            const data = JSON.parse(e.data);
            console.log(data);
        };

        return () => ws.close();
    }, []);

    return (
        <>
            <Viewer />
        </>
    )
}
