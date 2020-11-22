import Control.Concurrent.MVar
import Control.Concurrent

data BurgMach = BurgMach Int Int Int

getColaAmount :: BurgMach -> Int
getColaAmount (BurgMach cola pnorte quate) = cola
getPNorteAmount :: BurgMach -> Int
getPNorteAmount (BurgMach cola pnorte quate) = pnorte
getQuateAmount :: BurgMach -> Int
getQuateAmount (BurgMach cola pnorte quate) = quate

getCola :: MVar BurgMach -> Int -> MVar Int -> IO()
getCola machMvar clientId fimMvar
  = do 
    mach <- takeMVar machMvar
    threadDelay 1000000
    putStrLn ("O cliente "++ (show clientId) ++" do refrigerante P-Cola esta enchendo seu copo")
    if(getColaAmount mach - 300 < 1000)
    then (do 
            threadDelay 1500000
            putStrLn ("O refrigerante P-Cola foi reabastecido com 1000 ml, e agora possui "++ (show (getColaAmount mach + 1000 - 300))++" ml")
            putMVar machMvar (BurgMach (getColaAmount mach + 1000 - 300) (getPNorteAmount mach) (getQuateAmount mach)))
    else (do putMVar machMvar (BurgMach (getColaAmount mach - 300) (getPNorteAmount mach) (getQuateAmount mach)))
    putMVar fimMvar 0
    
getColas :: Int -> MVar BurgMach -> MVar Int -> IO()
getColas 0 _ fimMvar = do putMVar fimMvar 0
getColas x mach fimMvar = do 
                    child1Mvar <- newEmptyMVar
                    child2Mvar <- newEmptyMVar
                    forkIO(getCola mach x child1Mvar)
                    forkIO(getColas (x-1) mach child2Mvar)
                    takeMVar child1Mvar
                    takeMVar child2Mvar
                    putMVar fimMvar 0


getPNorte :: MVar BurgMach -> Int -> MVar Int -> IO()
getPNorte machMvar clientId fimMvar
  = do 
    mach <- takeMVar machMvar
    threadDelay 1000000
    putStrLn ("O cliente "++ (show clientId) ++" do refrigerante Guarana Polo Norte esta enchendo seu copo")
    if(getPNorteAmount mach - 300 < 1000)
    then (do 
            threadDelay 1500000
            putStrLn ("O refrigerante Guarana Polo Norte foi reabastecido com 1000 ml, e agora possui "++ (show (getPNorteAmount mach + 1000 - 300))++" ml")
            putMVar machMvar (BurgMach (getColaAmount mach) (getPNorteAmount mach + 1000 - 300) (getQuateAmount mach)))
    else (do putMVar machMvar (BurgMach (getColaAmount mach) (getPNorteAmount mach - 300) (getQuateAmount mach)))
    putMVar fimMvar 0
    
getPNortes :: Int -> MVar BurgMach -> MVar Int -> IO()
getPNortes 0 _ fimMvar = do putMVar fimMvar 0
getPNortes x mach fimMvar = do 
                    child1Mvar <- newEmptyMVar
                    child2Mvar <- newEmptyMVar
                    forkIO(getPNorte mach x child1Mvar)
                    forkIO(getPNortes (x-1) mach child2Mvar)
                    takeMVar child1Mvar
                    takeMVar child2Mvar
                    putMVar fimMvar 0    
    
getQuate :: MVar BurgMach -> Int -> MVar Int -> IO()
getQuate machMvar clientId fimMvar
  = do 
    mach <- takeMVar machMvar
    threadDelay 1000000
    putStrLn ("O cliente "++ (show clientId) ++" do refrigerante Guarana Quate esta enchendo seu copo")
    if(getQuateAmount mach - 300 < 1000)
    then (do 
            threadDelay 1500000
            putStrLn ("O refrigerante Guarana Quate foi reabastecido com 1000 ml e agora possui "++ (show (getQuateAmount mach + 1000 - 300))++" ml")
            putMVar machMvar (BurgMach (getColaAmount mach) (getPNorteAmount mach) (getQuateAmount mach + 1000 - 300)))
    else (do putMVar machMvar (BurgMach (getColaAmount mach) (getPNorteAmount mach) (getQuateAmount mach -300)))
    putMVar fimMvar 0
    
getQuates :: Int -> MVar BurgMach -> MVar Int -> IO()
getQuates 0 _ fimMvar = do putMVar fimMvar 0
getQuates x mach fimMvar = do 
                    child1Mvar <- newEmptyMVar
                    child2Mvar <- newEmptyMVar
                    forkIO(getQuate mach x child1Mvar)
                    forkIO(getQuates (x-1) mach child2Mvar)
                    takeMVar child1Mvar
                    takeMVar child2Mvar
                    putMVar fimMvar 0

main :: IO ()
main = do
  putStrLn "Digite os numeros de clientes de cada refrigerante separados por espacos"
  ln <- getLine
  let nums = map (read :: String -> Int) (words ln)

  fimq <- newEmptyMVar
  fimn <- newEmptyMVar
  fimc <- newEmptyMVar

  burgMVar <- newMVar (BurgMach 2000 2000 2000)

  forkIO(getColas (nums!!0) burgMVar fimc)
  forkIO(getPNortes (nums!!1) burgMVar fimn)
  forkIO(getQuates (nums!!2) burgMVar fimq)

  takeMVar fimc
  takeMVar fimn
  takeMVar fimq
  return ()
