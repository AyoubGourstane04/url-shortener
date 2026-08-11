export const isURLValid = (url: string): boolean => {
    try{
        const vURL = new URL(url);
        
        return vURL.protocol === 'http' || vURL.protocol === 'https';
    }catch{
        return false;
    }
}