import { Link, QrCode } from "lucide-react";
import { useState } from "react";
import { ResponseURL } from "../components/ResponseURL";
import { ResponseQR } from "../components/ResponseQR";
import type { Tab, TabId } from "../types/projTypes";
import { shortenUrl } from "../api/urlApi";


export const InputCard = () => {

    const [activeTab, setActiveTab] = useState<TabId>('shorten-url');

    const tabs: Tab[] = [
        {id: 'shorten-url', label: 'Shorten A URL'},
        {id: 'qr-url', label: 'Generate QR Code'},
    ];

    const [urlToShorten, setUrlToShorten] = useState<string>("");

    const [shortUrl, setShortUrl] = useState<string>("");


    const handleShorten = () => {
        const request = {url: urlToShorten};
        shortenUrl(request).then(res => setShortUrl(res));
    };

    const [generateClicked, isGenerateClicked] = useState(false);
    const [urlToGenerate, setUrlToGenerate] = useState<string>("");

    const handleGenerate = () =>{
        isGenerateClicked(true);
    }

    const clickButton = (btnId: string): void => {
        const btn = document.querySelector<HTMLButtonElement>(`#${btnId}`);
        btn?.click();
    }

    const handleEnterClicked = (event: KeyboardEvent, targetOp: string): void => {
        if(event.key === 'Enter'){
            event.preventDefault();
            // event.stopPropagation();
            event.stopImmediatePropagation();
            switch (targetOp){
                case 'shorten':
                    clickButton("shortenBtn");
                    break;
                case 'qrGenerate':
                    clickButton("qrGenerateBtn");
                    break;
                default:
                    return;
            }
        }
    }

    const shortenInput = document.querySelector<HTMLInputElement>("#shortenInput");
    shortenInput?.addEventListener("keydown", (e) => handleEnterClicked(e, "shorten"))

    const qrGenerateInput = document.querySelector<HTMLInputElement>("#qrGenerateInput");
    qrGenerateInput?.addEventListener("keydown", (e) => handleEnterClicked(e, "qrGenerate"));





    return (
      <section className="w-full max-w-3xl mx-auto px-4 pb-10">
            <div className="rounded-2xl border border-border/60 bg-card shadow-lg shadow-black/5 overflow-hidden">
                <div className="flex items-center gap-1 border-b border-border/60 bg-muted/20 px-4">
                    {tabs.map((tab) => (
                        <button
                            key={tab.id}
                            type="button"
                            onClick={() => setActiveTab(tab.id)}
                            className={`curson-pointer relative border-b-2 px-5 py-4 text-sm font-medium transition-all duration-200 focus:outline-none 
                                ${activeTab == tab.id ? 'border-primary text-primary' : 'border-transparent text-muted-foreground hover:border-muted-foreground/40 hover:text-foreground'}`}
                        >
                            {tab.label}
                        </button>
                    ))}
                </div>

                            
                {activeTab === 'shorten-url' && (
                    <div className="border border-border/60 bg-card shadow-lg shadow-black/5 p-6 sm:p-8"> 
                        <div className="mb-7">
                            <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl border border-primary/20 bg-primary/10">
                                <Link className="h-6 w-6 text-primary" />
                            </div>

                            <h2 className="text-xl font-semibold text-foreground">
                                Shorten a URL
                            </h2>

                            <p className="mt-2 text-sm text-muted-foreground">
                                Paste your long link below and create a short, shareable URL.
                            </p>
                        </div>

                        <div className="flex flex-col gap-3 sm:flex-row">
                            <input 
                                    type="url" 
                                    id="shortenInput"
                                    placeholder="https://example.com/your-long-url"
                                    className="min-w-0 flex-1 rounded-xl border border-border bg-background px-4 py-3 text-sm text-foreground
                                        outline-none transition-colors placeholder:text-muted-foreground focus:border-primary focus:ring-2 focus:ring-primary/20"     
                                    onChange={(e) =>{ 
                                        setUrlToShorten(e.target.value);
                                        setShortUrl("");
                                    }}
                                    onInput={() =>  setShortUrl("")}
                            />
                            <button 
                                type="button"
                                id="shortenBtn"
                                className="cursor-pointer rounded-xl bg-primary px-6 py-3 text-sm font-medium text-primary-foregorund transition-all hover:opacity-90 active:scale-[0.98]"      
                                onClick={handleShorten}
                            >
                                Shorten URL
                            </button>
                        </div>
                       {shortUrl && <div className="mt-4 space-y-3 mb-3">
                            <ResponseURL url={shortUrl}/>
                            <ResponseQR url={shortUrl}/>
                        </div>}
                       
                        
                    </div>  
                )}

                {activeTab === 'qr-url' && (
                    <div className="border border-border/60 bg-card shadow-lg shadow-black/5 p-6 sm:p-8"> 
                        <div className="mb-7">
                            <div className="mb-4 flex h-12 w-12 items-center justify-center rounded-xl border border-primary/20 bg-primary/10">
                                <QrCode className="h-6 w-6 text-primary" />
                            </div>

                            <h2 className="text-xl font-semibold text-foreground">
                                Generate a QR Code
                            </h2>

                            <p className="mt-2 text-sm text-muted-foreground">
                                Turn any URL into a QR code that you can scan and share.
                            </p>
                        </div>

                        <div className="flex flex-col gap-3 sm:flex-row">
                            <input 
                                    type="url"                                 
                                    id="qrGenerateInput"
                                    placeholder="https://example.com"
                                    className="min-w-0 flex-1 rounded-xl border border-border bg-background px-4 py-3 text-sm text-foreground
                                        outline-none transition-colors placeholder:text-muted-foreground focus:border-primary focus:ring-2 focus:ring-primary/20"     
                                    onChange={(e) => {
                                        setUrlToGenerate(e.target.value);
                                        isGenerateClicked(false);
                                    }}
                            />
                            <button 
                                id="qrGenerateBtn"     
                                type="button"
                                className="cursor-pointer rounded-xl bg-primary px-6 py-3 text-sm font-medium text-primary-foregorund transition-all hover:opacity-90 active:scale-[0.98]"    
                                onClick={handleGenerate}
                            >
                                Generate QR
                            </button>
                        </div>
                        {generateClicked  && <div className="mt-4">
                                <ResponseQR url={urlToGenerate}/>
                            </div>
                        }
                    </div>  
                    
                )}
            </div>
      </section>
    )
}