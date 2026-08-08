import type { ShortenUrlRequest, ShortenUrlResponse } from "../types/projTypes";
import { apiRequest } from "./apiClient";


export const shortenUrl  = async (request : ShortenUrlRequest): Promise<string> => {
    const response = await apiRequest<ShortenUrlResponse>('shorten' , {
                    method : "POST",
                    body : JSON.stringify(request),
                });
    return response.shortUrl;
}


