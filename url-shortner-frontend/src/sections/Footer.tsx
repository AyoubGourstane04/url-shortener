import type React from "react";

export const Footer : React.FC = () =>  {
    const currentYear = new Date().getFullYear();
    

    return (
        <footer className="w-full py-6 border-t border-border/30 animate-fade-in text-center mt-auto">
            <p className="text-xs sm:text-sm text-muted-foreground">
               © {currentYear} URL Shortener. All rights reserved.
            </p>

           
        </footer>
    )
} 