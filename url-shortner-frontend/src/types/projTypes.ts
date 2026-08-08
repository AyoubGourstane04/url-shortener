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