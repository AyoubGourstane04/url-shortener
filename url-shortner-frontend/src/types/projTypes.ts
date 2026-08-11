export type TabId = 'shorten-url' | 'qr-url';

export interface Tab{
    id: TabId;
    label: string;
};

export interface ShortenUrlRequest{
    url: string;
};

export interface ShortenUrlResponse{
    shortCode: string;
    shortUrl: string;
};

export interface GenerateQrCodeRequest{
    url: string;
};

export interface GenerateQrCodeResponse{
    url: string;
    shortUrl: string;
    qrCodePath: string;
};

export type PopUpType = 'success' | 'error' | null;

export interface Message{
    state: PopUpType;
    message: string;
};


export interface PopUpMessageProps{
    message: Message | null;
    setMessage: (msg: Message | null) => void;

}