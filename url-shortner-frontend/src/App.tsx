import './App.css'
import { InputCard } from './sections/InputCard';
import { Footer } from './sections/Footer';
import { Header } from './sections/Header';


function App() {
  return (
        <div className="min-h-screen bg-background text-foreground flex flex-col">
            <Header />
            <main className='flex-1 w-full'>
              <InputCard />
            </main>
            <Footer />
        </div>
  );
}

export default App
