import { QRCodeCanvas } from "qrcode.react";

export const ResponseQR = ({url}: {url: string}) => {


    const handleDownload = () => {
        const canvas = document.getElementById("qrCode") as HTMLCanvasElement;
        if(!canvas) return;

        const pngUrl = canvas.toDataURL("image/png");

        const downloadLink = document.createElement('a');
        downloadLink.href = pngUrl;
        downloadLink.download = "shortened-url-qr-code.png";

        document.body.appendChild(downloadLink);
        
        downloadLink.click();
    
        document.body.removeChild(downloadLink);
    };


    const handleShare = async () => {
        const canvas = document.getElementById("qrCode") as HTMLCanvasElement;
        if(!canvas) return;

        canvas.toBlob(async (blob) => {
            if(!blob) return;

            const file = new File(
                [blob],
                "QR-Code.png",
                {
                    type: "image/png",
                }
            );

            if(navigator.canShare && navigator.canShare({files: [file],})){
                try {
                    await navigator.share({
                        title: "QR Code",
                        text: "Scan this QR Code to open the shortened URL",
                        files: [file],
                    });
                } catch (error) {
                    console.error("Unable to share:", error);
                }
            }else {
                alert("Image sharing is not supported on this browser. You can share the link instead: " + url);
            }
        }, "image/png");
    };





 return (
        <div className="w-full">
            <div className="overflow-hidden rounded-2xl border border-primary/20 bg-card p-2 shadow-xl shadow-black/20">
                <div className="flex items-center gap-2 px-3 pb-3 pt-2">
                    <div className="flex h-7 w-7 items-center justify-center rounded-md bg-primary/10" >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth="2"
                            className="h-4 w-4 text-primary"
                        >
                            <rect x="3" y="3" width="7" height="7" />
                            <rect x="14" y="3" width="7" height="7" />
                            <rect x="3" y="14" width="7" height="7" />
                            <path d="M14 14h3v3h-3z" />
                            <path d="M18 18h3v3h-3z" />
                            <path d="M14 21v-3" />
                            <path d="M21 14v3" />
                        </svg>
                    </div>
                    <div>
                        <p className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
                            Your QR Code 
                        </p>
                        <p className="text-xs text-muted-foreground/70">
                            Scan to open your URL
                        </p>
                    </div>
                </div>

                <div className="flex flex-col items-center rounded-xl border border-border bg-surface px-5 py-8">
                   <div className="rounded-2xl bg-white p-4 shadow-lg shadow-black/20">
                        <QRCodeCanvas id="qrCode" value={url} size={200}/>
                   </div>
                   <p className="mt-5 text-center text-sm text-muted-foreground">
                        Scan this code with your phone camera
                   </p>
                </div>
                <div className="mt-4 grid grid-cols-1 gap-3 sm:grid-cols-2">
                    <button
                        className="
                            group flex items-center justify-center gap-2.5
                            rounded-xl bg-primary px-5 py-3.5
                            text-sm font-semibold text-primary-foreground
                            shadow-lg shadow-primary/20
                            transition-all duration-200
                            hover:-translate-y-0.5
                            hover:brightness-110
                            hover:shadow-xl hover:shadow-primary/25
                            active:translate-y-0
                            active:scale-[0.98]
                        "
                        onClick={handleDownload}
                    >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            className="h-5 w-5 transition-transform duration-200 group-hover:translate-y-0.5"
                        >
                            <path d="M12 3v12" />
                            <path d="m7 10 5 5 5-5" />
                            <path d="M5 21h14" />
                        </svg>

                        <span>Download</span>
                    </button>

                    <button
                        type="button"
                        onClick={handleShare}
                        className="
                            group flex items-center justify-center gap-2.5
                            rounded-xl border border-border
                            bg-muted px-5 py-3.5
                            text-sm font-semibold text-foreground
                            transition-all duration-200
                            hover:-translate-y-0.5
                            hover:border-primary/50
                            hover:bg-primary/10
                            hover:text-primary
                            hover:shadow-lg hover:shadow-black/10
                            active:translate-y-0
                            active:scale-[0.98]
                        "
                    >
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            strokeWidth="2"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            className="h-5 w-5 transition-transform duration-200 group-hover:scale-110"
                        >
                            <circle cx="18" cy="5" r="3" />
                            <circle cx="6" cy="12" r="3" />
                            <circle cx="18" cy="19" r="3" />

                            <path d="m8.6 13.5 6.8 4" />
                            <path d="m15.4 6.5-6.8 4" />
                        </svg>

                        <span>Share QR Code</span>
                    </button>
                </div>
               
            </div>
        </div>
    );
}