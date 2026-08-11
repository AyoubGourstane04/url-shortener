import { AlertCircle, CheckCircle } from "lucide-react";
import { useEffect, type JSX } from "react";
import type { PopUpMessageProps } from "../types/projTypes";


export const MessagePopUp  = ({message, setMessage}: PopUpMessageProps): JSX.Element => {
    
    useEffect(() => {
        if(message && message.state){
            const timer = setTimeout(() => {
                setMessage({ state: null, message: ""});
            }, 3000);

            return () => clearTimeout(timer);
        }
    }, [message, setMessage]);

    if(!message || !message.state) return <></>;

    return (<div className={`flex items-center gap-3 p-4 rounded-xl ${
                    message.state === "success"
                        ? "bg-green-500/10 border border-green-500/20 text-green-400"
                        : "bg-red-500/10 border border-red-500/20 text-red-400"
                }`}
            >
                {message.state === "success" ? (
                    <CheckCircle className="w-5 h-5 shrink-0"/>
                ):(
                    <AlertCircle className="w-5 h-5 shrink-0"/>
                )}
                <p className="text-sm">{message.message}</p>
            </div>   
    );
}