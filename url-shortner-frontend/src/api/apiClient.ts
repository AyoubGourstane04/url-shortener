
const API_BASE_URL = "http://localhost:8080/api/v1/";


export async function apiRequest<T>(
    endpoint: string,
    options: RequestInit = {}
) : Promise<T> {

    const {headers, ...restOptions} = options; 
    
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        headers:{
            "Content-Type": "application/json",
            ...headers,
        },
        mode: "cors",
        ...restOptions,
    });

    if(!response.ok){
        throw new Error("Something went wrong with the API request");
    }

    return response.json() as Promise<T>;
}