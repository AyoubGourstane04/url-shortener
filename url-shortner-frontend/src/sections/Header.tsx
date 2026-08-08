import { Link } from "lucide-react"
import type React from "react"


export const Header : React.FC = () =>  {
    return (
        <header className="w-full pt-10 pb-6 px-4 flex flex-col items-center justify-center animate-fade-in text-center">
            <div className="inline-flex items-center justify-center p-3 bg-primary/10 rounded-2xl mb-5 border border-primary/20 shadow-sm">
                <Link className="w-9 h-9 text-primary"/>
            </div>
            
            <h1 className="text-3xl md:text-4xl font-bold tracking-light text-foreground mb-3">
                URL Shortener
            </h1>

            <p className="text-muted-foreground text-sm md:text-base max-w-md mx-auto">
                Shorten links, track clicks, and simplify your sharing
            </p>
        </header>
    )
} 