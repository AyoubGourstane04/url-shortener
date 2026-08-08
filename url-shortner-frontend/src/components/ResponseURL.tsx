import { useState } from "react";


export const ResponseURL = ({url = ""}: {url?: string}) => {
    const [copied, setCopied] = useState(false);

    const handleCopy = async () => {
        await navigator.clipboard.writeText(url);

        setCopied(true);

        setTimeout(() => {
            setCopied(false);
        }, 1500);
    };

    return (
        <div className="w-full">
            <div className="rounded-2xl border border-primary/20 bg-card p-2 shadow-xl shadow-black/20">
                
                <div className="flex items-center gap-2 px-3 pb-2 pt-1">
                    <div className="h-2 w-2 rounded-full bg-primary" />

                    <span className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
                        Your shortened URL
                    </span>
                </div>

                <div className="flex min-w-0 items-center gap-3 rounded-xl border border-border bg-surface px-4 py-3">
                    
                    <span className="min-w-0 flex-1 truncate text-base font-medium text-foreground sm:text-lg">
                        {url}
                    </span>

                    <button
                        type="button"
                        onClick={handleCopy}
                        aria-label="Copy shortened URL"
                        className={`
                            flex h-11 w-11 shrink-0 items-center justify-center
                            rounded-lg border transition-all duration-200
                            ${
                                copied
                                    ? "border-emerald-500/40 bg-emerald-500/10 text-emerald-400"
                                    : "border-border bg-muted text-muted-foreground hover:border-primary/50 hover:bg-primary/10 hover:text-primary"
                            }
                        `}
                    >
                        {copied ? (
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                strokeWidth="2.5"
                                className="h-5 w-5"
                            >
                                <path d="m5 12 4 4L19 6" />
                            </svg>
                        ) : (
                            <svg
                                xmlns="http://www.w3.org/2000/svg"
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                strokeWidth="2"
                                className="h-5 w-5"
                            >
                                <rect
                                    x="9"
                                    y="3"
                                    width="12"
                                    height="14"
                                    rx="2"
                                />

                                <path d="M5 7H4a2 2 0 0 0-2 2v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2v-1" />
                            </svg>
                        )}
                    </button>
                </div>
            </div>

            <div
                className={`
                    overflow-hidden transition-all duration-300
                    ${
                        copied
                            ? "mt-2 max-h-10 opacity-100"
                            : "mt-0 max-h-0 opacity-0"
                    }
                `}
            >
                <p className="flex items-center justify-center gap-2 text-sm font-medium text-emerald-400">
                    <span>✓</span>
                    URL copied to clipboard
                </p>
            </div>
        </div>
    );
};